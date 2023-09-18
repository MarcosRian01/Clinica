package med.voll.api.paciente;

public record ListarDadosPaciente(Long id, String nome, String cpf, String email) {
	
	public ListarDadosPaciente(Paciente paciente) {
		this(
				paciente.getId(), 
				paciente.getNome(), 
				paciente.getCpf(), 
				paciente.getEmail());
	}
	
}
