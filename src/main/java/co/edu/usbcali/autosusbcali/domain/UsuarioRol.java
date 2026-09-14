package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios_roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioRol {
    @EmbeddedId
    private UsuarioRolId id;
}