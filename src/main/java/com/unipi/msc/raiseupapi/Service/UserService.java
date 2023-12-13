package com.unipi.msc.raiseupapi.Service;

import com.unipi.msc.raiseupapi.Interface.IUser;
import com.unipi.msc.raiseupapi.Model.Image;
import com.unipi.msc.raiseupapi.Model.User;
import com.unipi.msc.raiseupapi.Repository.ImageRepository;
import com.unipi.msc.raiseupapi.Repository.UserRepository;
import com.unipi.msc.raiseupapi.Request.EditUserRequest;
import com.unipi.msc.raiseupapi.Response.GenericResponse;
import com.unipi.msc.raiseupapi.Response.UserPresenter;
import com.unipi.msc.raiseupapi.Shared.ErrorMessages;
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

@Component
@RequiredArgsConstructor
public class UserService implements IUser {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;

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
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
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
