package com.unipi.msc.riseupapi.Service;

import com.unipi.msc.riseupapi.Interface.IStep;
import com.unipi.msc.riseupapi.Interface.ITask;
import com.unipi.msc.riseupapi.Model.*;
import com.unipi.msc.riseupapi.Repository.*;
import com.unipi.msc.riseupapi.Request.ColumnRequest;
import com.unipi.msc.riseupapi.Response.BoardPresenter;
import com.unipi.msc.riseupapi.Response.GenericResponse;
import com.unipi.msc.riseupapi.Shared.ErrorMessages;
import com.unipi.msc.riseupapi.Shared.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StepService implements IStep {
    private final ITask iTask;
    private final StepRepository stepRepository;
    @Override
    public ResponseEntity<?> editStep(Long columnId, ColumnRequest request) {
        Step step = stepRepository.findById(columnId).orElse(null);
        if (step == null) return GenericResponse.builder().message(ErrorMessages.STEP_NOT_FOUND).build().badRequest();
        if (request.getTitle()!=null){
            step.setTitle(request.getTitle());
            step = stepRepository.save(step);
        }
        if (request.getPosition()!=null){
            List<Step> boardSteps = stepRepository.findAllByBoardOrderByPositionAsc(step.getBoard());
            long oldPosition = step.getPosition();
            long newPosition = request.getPosition();
            if (newPosition>oldPosition){
                boardSteps.stream()
                    .filter(boardStep -> oldPosition <= boardStep.getPosition() && boardStep.getPosition() < newPosition)
                    .forEach(boardStep -> {
                        boardStep.setPosition(boardStep.getPosition()-1);
                        stepRepository.save(boardStep);
                    });
            }else {
                boardSteps.stream()
                        .filter(boardStep -> newPosition <= boardStep.getPosition() && boardStep.getPosition() < oldPosition)
                        .forEach(boardStep -> {
                            boardStep.setPosition(boardStep.getPosition()+1);
                            stepRepository.save(boardStep);
                        });
            }
            step.setPosition(newPosition);
            step = stepRepository.save(step);
        }
        BoardPresenter presenter = BoardPresenter.getPresenter(step.getBoard());
        return GenericResponse.builder().data(presenter).build().success();
    }

    @Override
    public ResponseEntity<?> deleteStep(Long stepId) {
        Step step = stepRepository.findById(stepId).orElse(null);
        if (step == null) return GenericResponse.builder().message(ErrorMessages.STEP_NOT_FOUND).build().badRequest();
//        //delete Tasks
        for (Task task : step.getTasks()){
            ResponseEntity<?> responseEntity = iTask.deleteTask(task.getId());
            if (((GenericResponse<?>)responseEntity.getBody()).getCode() != Tags.HTTP_OK) {
                return responseEntity;
            }
        }
        step.getTasks().clear();
        step = stepRepository.save(step);

        stepRepository.delete(step);

        return GenericResponse.builder().build().success();
    }
}
