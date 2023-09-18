package med.voll.api.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import med.voll.api.endereco.DadosEndereco;

public record atualizarPaciente(
		@NotNull
		Long id, 
		String nome,
		String cpf,
		@Valid
		DadosEndereco endereco
		) {
	
}
