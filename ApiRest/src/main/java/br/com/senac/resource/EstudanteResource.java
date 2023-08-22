package br.com.senac.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senac.commons.ExampleValues;
import br.com.senac.dto.EstudanteDTO;
import br.com.senac.entity.Estudante;
import br.com.senac.service.EstudanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("estudantes")
public class EstudanteResource {

	@Autowired
	private EstudanteService estudanteService;
	
	@Autowired
	private ModelMapper mapper;
	
	@GetMapping
	public ResponseEntity<List<EstudanteDTO>> buscarTodosEstudantes() {
		List<Estudante> listaEstudantes =	estudanteService.buscarTodosEstudantes();
		List<EstudanteDTO> listaEstudantesDTO = listaEstudantes.stream().map(
				estudante -> mapper.map(estudante, EstudanteDTO.class)).collect(Collectors.toList());
				
		return ResponseEntity.ok().body(listaEstudantesDTO);
	}

	
	@PostMapping
	@Operation(description = "Cadastrar estudante")
	public ResponseEntity<EstudanteDTO> cadastrarEstudante(@RequestBody 
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
					examples = {@ExampleObject(
												name="Exemplo de Estudante",
												value = ExampleValues.exemploEstudante)})
					)
			
					EstudanteDTO estudanteDTO) {
		Estudante estudante = mapper.map(estudanteDTO, Estudante.class);
		estudante = estudanteService.salvar(estudante);
		EstudanteDTO novoEstudante = mapper.map(estudante, EstudanteDTO.class);
		return ResponseEntity.ok().body(novoEstudante);
	}

	@GetMapping("/{id}")
	@Operation(description = "Retorna o registro do estudante por id")
	public ResponseEntity<EstudanteDTO> buscarEstudantePeloID(@PathVariable("id") 
	@Schema(example = ExampleValues.idEstudante) Integer id){
		
		Estudante estudante = estudanteService.getEstudanteByID(id);
		EstudanteDTO estudanteDTO = mapper.map(estudante, EstudanteDTO.class);
		return ResponseEntity.ok().body(estudanteDTO);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EstudanteDTO> atualizarEstudante(@PathVariable("id") Integer id,
			@RequestBody EstudanteDTO estudanteDTO){
		
		Estudante estudante = mapper.map(estudanteDTO, Estudante.class);
		estudante = estudanteService.updateEstudante(id, estudante);
		
		EstudanteDTO novoEstudante = mapper.map(estudante, EstudanteDTO.class);
		
		return ResponseEntity.ok().body(novoEstudante);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> excluirEstudante(@PathVariable("id") Integer id){
		Boolean flag = estudanteService.deleteEstudante(id);
		return ResponseEntity.ok().body(flag);
	}
	
	
	
	
	
}



