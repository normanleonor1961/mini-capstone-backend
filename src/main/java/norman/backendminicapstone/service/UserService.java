package norman.backendminicapstone.service;

import norman.backendminicapstone.dto.UserDTO;
import norman.backendminicapstone.entity.UserEntity;
import norman.backendminicapstone.exception.UserAlreadyExist;
import norman.backendminicapstone.model.UserRequest;
import norman.backendminicapstone.repository.UserRepository;
import norman.backendminicapstone.util.DateTimeUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DateTimeUtil dateTimeUtil;
    private final ModelMapper modelMapper;

    public UserDTO saveUser(@NonNull UserRequest newUser) {

        // Check if email is existing
        if(userRepository.findByEmail(newUser.getEmail()) != null) {
            throw new UserAlreadyExist("Email already in used");
        }

        // Initialize user
        UserEntity user = UserEntity
                .builder()
                .userId(UUID.randomUUID())
                .email(newUser.getEmail())
                .password(newUser.getPassword())
                .totalOrders(0)
                .successOrders(0)
                .createdDate(dateTimeUtil.currentDate())
                .modifiedDate(dateTimeUtil.currentDate())
                .build();

        // Save to database
        userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    public String deleteUser(String email) {
        String response = "No data has been deleted";

        // Get user
        UserEntity user = userRepository.findByEmail(email);

        // Check if user exist
        if(user != null) {
            userRepository.deleteByEmail(user.getEmail());
            response = email + " has been successfully deleted";
        }

        return response;
    }

    public UserDTO updateUser(String oldEmail, UserRequest userRequest) {
        // Initialize user
        UserEntity user = userRepository.findByEmail(oldEmail);

        // Check if user is existing
        if(user == null) throw new UserAlreadyExist("User doesn't exist");

        // update user
        UserEntity updatedUser = UserEntity
                .builder()
                .userId(user.getUserId())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .totalOrders(user.getTotalOrders())
                .successOrders(user.getSuccessOrders())
                .createdDate(user.getCreatedDate())
                .modifiedDate(dateTimeUtil.currentDate())
                .build();

        // Check if new email exist
        if(userRepository.findByEmail(updatedUser.getEmail()) != null) {
            throw new UserAlreadyExist("Email already in used");
        }

        // save updated user
        userRepository.save(updatedUser);

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    public UserDTO loginUser(@NonNull UserRequest activeUser) {
        // Initialize User
        UserEntity user = userRepository.findByEmail(activeUser.getEmail());

        // Check if user existing
        if (user == null) throw new UserAlreadyExist("User doesn't exist");

        // Check if email is existing
        if(!Objects.equals(user.getPassword(), activeUser.getPassword())) throw new UserAlreadyExist("Invalid password");

        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO loginByProvider(@NonNull String email) {
        // Initialize User
        UserEntity user = userRepository.findByEmail(email);

        // Check if email is existing
        if (user == null) {
            // Initialize new user
            UserEntity newUser = UserEntity
                    .builder()
                    .userId(UUID.randomUUID())
                    .email(email)
                    .password(null)
                    .totalOrders(0)
                    .successOrders(0)
                    .createdDate(dateTimeUtil.currentDate())
                    .modifiedDate(dateTimeUtil.currentDate())
                    .build();

            // Save to database
            userRepository.save(newUser);

            return modelMapper.map(newUser, UserDTO.class);
        }

        return modelMapper.map(user, UserDTO.class);
    }

}
