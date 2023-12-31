package com.unipi.msc.raiseupapi.Service;

import com.unipi.msc.raiseupapi.Interface.IStep;
import com.unipi.msc.raiseupapi.Model.Board;
import com.unipi.msc.raiseupapi.Model.Step;
import com.unipi.msc.raiseupapi.Repository.BoardRepository;
import com.unipi.msc.raiseupapi.Repository.StepRepository;
import com.unipi.msc.raiseupapi.Request.ColumnRequest;
import com.unipi.msc.raiseupapi.Response.BoardPresenter;
import com.unipi.msc.raiseupapi.Response.GenericResponse;
import com.unipi.msc.raiseupapi.Shared.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
