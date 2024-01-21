package com.unipi.msc.riseupapi.Service;

import com.unipi.msc.riseupapi.Interface.IUser;
import com.unipi.msc.riseupapi.Model.Board;
import com.unipi.msc.riseupapi.Model.Image;
import com.unipi.msc.riseupapi.Model.User;
import com.unipi.msc.riseupapi.Repository.BoardRepository;
import com.unipi.msc.riseupapi.Repository.ImageRepository;
import com.unipi.msc.riseupapi.Repository.UserRepository;
import com.unipi.msc.riseupapi.Request.EditUserRequest;
import com.unipi.msc.riseupapi.Request.FCMRequest;
import com.unipi.msc.riseupapi.Response.GenericResponse;
import com.unipi.msc.riseupapi.Response.UserPresenter;
import com.unipi.msc.riseupapi.Shared.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserService implements IUser {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
    private final BoardRepository boardRepository;
    @Override
    public ResponseEntity<?> getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return GenericResponse.builder().data(UserPresenter.getPresenter(user)).build().success();
    }
    @Override
    public ResponseEntity<?> editUser(EditUserRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (request.getFirstName()!=null) user.setFirstName(request.getFirstName());
        if (request.getLastName()!=null) user.setLastName(request.getLastName());
        if (request.getPassword()!=null) user.setPassword(passwordEncoder.encode(request.getLastName()));
        if (request.getMultipartFile()!=null){
            Image image = saveFile(request.getMultipartFile(),String.valueOf(user.getId()));
            if (image == null) return GenericResponse.builder().message(ErrorMessages.SOMETHING_HAPPENED).build().badRequest();
            user.setImage(image);
        }
        user = userRepository.save(user);
        return GenericResponse.builder().data(UserPresenter.getPresenter(user)).build().success();
    }
    @Override
    public ResponseEntity<?> getUserImage(Long userId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            File file = new File(getDirPath() + File.separator + user.getImage().getFileName());
            InputStream targetStream = new FileInputStream(file);
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.IMAGE_PNG)
                    .body(IOUtils.toByteArray(targetStream));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @Override
    public ResponseEntity<?> searchUser(Long boardId, boolean allUsers, String keyword) {
        List<User> userList;
        List<UserPresenter> presenters = new ArrayList<>();

        User client = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (keyword.isEmpty()){
            userList = userRepository.findAll();
        }else{
            userList = userRepository.findUsersByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContaining(keyword,keyword,keyword,keyword);
        }
        if (boardId!=0L){
            Board board = boardRepository.findById(boardId).orElse(null);
            if (board == null) return GenericResponse.builder().message(ErrorMessages.BOARD_NOT_FOUND).build().badRequest();
            if (allUsers){
                userList.remove(board.getOwner());
                presenters = UserPresenter.getPresenter(userList);
            } else {
                for (User u :userList){
                    if (u.getBoards().contains(board) || u == board.getOwner()) {
                        presenters.add(UserPresenter.getPresenter(u));
                    }
                }
            }
        }else {
            if (allUsers) userList.remove(userList.stream().filter(user -> Objects.equals(user.getId(), client.getId())).findFirst().get());
            presenters = UserPresenter.getPresenter(userList);
        }
        return GenericResponse.builder().data(presenters).build().success();

    }
    @Override
    public ResponseEntity<?> getUsers() {
        List<UserPresenter> users = UserPresenter.getPresenter(userRepository.findAll());
        return GenericResponse.builder().data(users).build().success();
    }
    @Override
    public ResponseEntity<?> updateUserFCM(FCMRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setFcmId(request.getToken());
        user = userRepository.save(user);
        UserPresenter presenter = UserPresenter.getPresenter(user);
        return GenericResponse.builder().data(presenter).build().success();
    }
    private Image saveFile(MultipartFile multipartFile, String fileName){

        Path dirPath = getDirPath();
        String fileExt = "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String fullFilename = fileName + fileExt;
        Path photoPath = Paths.get(dirPath + "/" + fullFilename);

        byte[] photoBytes;
        try {
            photoBytes = multipartFile.getBytes();
            Files.write(photoPath, photoBytes);
            return imageRepository.save(Image.builder()
                    .fileName(fullFilename)
                    .fileType(fileExt)
                    .build());
        } catch (IOException e) {
            return null;
        }
    }

    private static Path getDirPath() {
        Path currentPath = Paths.get("");
        Path absolutePath = currentPath.toAbsolutePath();
        Path dirPath = Paths.get(absolutePath + "/photos");

        File dirPhoto = dirPath.toFile();
        if (!dirPhoto.exists()) dirPhoto.mkdir();
        return dirPath;
    }

    private String getFileExtension(String fileName) {
        final int indexOfLastDot = fileName.lastIndexOf('.');
        return fileName.substring(indexOfLastDot + 1);
    }
}
