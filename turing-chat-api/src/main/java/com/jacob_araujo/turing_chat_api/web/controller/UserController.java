package com.jacob_araujo.turing_chat_api.web.controller;

import com.jacob_araujo.turing_chat_api.entity.User;
import com.jacob_araujo.turing_chat_api.service.UserService;
import com.jacob_araujo.turing_chat_api.web.dto.*;
import com.jacob_araujo.turing_chat_api.web.dto.mapper.UserMapper;
import com.jacob_araujo.turing_chat_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name= "Usuários", description = "Contém todas operações relativas aos recursos para cadastro, edição e leitura de usuários")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Criar um novo usuário",
            responses = {
                    @ApiResponse(responseCode="201", description="Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Token bearer inválido",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Email já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<UserResponseDto> create (@Valid @RequestBody UserCreateDto createDto){ // tem que converter o objeto dto dentro do controller, e não passar para o service porque como estamos criando o dto dentro da camada web so devemos usar o dto dentro da camada web
        User userCreated = userService.save(UserMapper.toUser(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(userCreated));
    }

    @Operation(summary = "Recuperar um usuário pelo id",
            responses = {
                    @ApiResponse(responseCode="200", description="Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('CLIENT') AND #id == authentication.principal.getId )")
    public ResponseEntity<UserResponseDto> getById (@PathVariable Long id){
        User user = userService.searchById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @Operation(summary = "Atualizar senha",
            responses = {
                    @ApiResponse(responseCode="204", description="Senha atualizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos ou mal formatados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT') AND #id == authentication.principal.getId")
    public ResponseEntity<Void> changePassword (@PathVariable Long id, @Valid @RequestBody UserPasswordDto dto){
        userService.updatePassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword (@RequestBody UserUsernameDto dto){
        userService.forgotPassword(dto.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword (@RequestBody UserResetPasswordDto dto){
        userService.resetPassword(dto.getResetToken(), dto.getNewPassword(), dto.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Usuários recuperados com sucesso",
            responses = {
                    @ApiResponse(responseCode="200", description="Recuso recuperado com sucesso com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto[].class)))
            })
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAll (){
        List<User> users = userService.searchAll();
        return ResponseEntity.ok(UserMapper.toListDto(users));
    }

    @Operation(summary = "Verificar email",
            responses = {
                    @ApiResponse(responseCode="200", description="Email verificado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Token inválido",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/verify-email/{token}")
    public ResponseEntity<UserResponseDto> verifyEmail (@PathVariable String token){
        User user = userService.verifyEmail(token);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

}
