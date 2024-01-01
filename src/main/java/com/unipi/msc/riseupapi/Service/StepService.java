package com.unipi.msc.riseupapi.Service;

import com.unipi.msc.riseupapi.Interface.IStep;
import com.unipi.msc.riseupapi.Model.Step;
import com.unipi.msc.riseupapi.Repository.StepRepository;
import com.unipi.msc.riseupapi.Request.ColumnRequest;
import com.unipi.msc.riseupapi.Response.BoardPresenter;
import com.unipi.msc.riseupapi.Response.GenericResponse;
import com.unipi.msc.riseupapi.Shared.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StepService implements IStep {
    private final StepRepository stepRepository;
    @Override
    public ResponseEntity<?> editTag(Long columnId, ColumnRequest request) {
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
}
