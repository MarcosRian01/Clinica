package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import med.voll.api.medico.DadosDetalhamentoMedico;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.DadosDetalhamentoPaciente;
import med.voll.api.paciente.ListarDadosPaciente;
import med.voll.api.paciente.Paciente;
import med.voll.api.paciente.PacienteRepository;
import med.voll.api.paciente.atualizarPaciente;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteRepository repository;
	
	
	/* Método POST para fazer o cadastro de um novo paciente */
	@PostMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoPaciente> cadastrar(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
		var paciente = new Paciente(dados);
		repository.save(paciente);
		
		/* Gerando a nova rota para o paciente cadastrado */
		var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
	}

	
	/* Método GET para retornar todos os pacientes já cadastrados */
	@GetMapping
	public ResponseEntity<Page<ListarDadosPaciente>> listar(Pageable pageable) {
		/* findAllByAtivoTrue para trazer apenas os pacientes ativos */
		var page = repository.findAllByAtivoTrue(pageable).map(ListarDadosPaciente::new);
		
		return ResponseEntity.ok(page);
	}
	
	
	/* Método GET para detalhar um paciente já existente */
	@GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPaciente> detalhar(@PathVariable Long id) {
        var paciente = repository.getReferenceById(id);
        
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }
	
	
	/* Método PUT para editar os dados de um paciente cadastrado */
	@PutMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoPaciente> atualizar(@RequestBody @Valid atualizarPaciente dados) {
		var paciente = repository.getReferenceById(dados.id());
		paciente.atualizarDados(dados);
		
		return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
	}
	
	
	/* Método DELETE para inativar um paciente */
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<DadosDetalhamentoPaciente> excluir(@PathVariable Long id) {
		var paciente = repository.getReferenceById(id);
		paciente.excluir();
		
		return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
	}
}