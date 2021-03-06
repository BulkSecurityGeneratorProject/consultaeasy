/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.friendscorp.consulteasy.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.friendscorp.consulteasy.domain.Contato;
import xyz.friendscorp.consulteasy.domain.EnumTipoContato;
import xyz.friendscorp.consulteasy.domain.Paciente;
import xyz.friendscorp.consulteasy.repository.ContatoRepository;
import xyz.friendscorp.consulteasy.repository.PacienteRepository;
import xyz.friendscorp.consulteasy.service.dto.ContatoDTO;

/**
 *
 * @author wellington
 */
@Service
@Transactional
public class ContatoService {
    
    @Autowired
    private ContatoRepository contatoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    public Contato contatoFromDTO(ContatoDTO contatoDTO) {
        System.out.println(contatoDTO);
        Paciente paciente = pacienteRepository.findOne(contatoDTO.getIdPaciente());
        Contato contato = new Contato(contatoDTO.getId(), 
                contatoDTO.getCodigoArea(), 
                contatoDTO.getContato(), 
                EnumTipoContato.valueOf(contatoDTO.getTipoContato()), 
                paciente);
        return contato;
    }
    
    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }
    
    public Contato getContato(Long id) {
        return contatoRepository.findOne(id);
    }
    
    public Contato createContato(ContatoDTO contatoDTO) {
        return this.contatoRepository.save(this.contatoFromDTO(contatoDTO));
    }
    
    public Optional<ContatoDTO> updateContato(ContatoDTO contatoDTO) {
        return Optional.of(contatoRepository.findOne(contatoDTO.getId()))
                .map(c -> {
                    EnumTipoContato tipoContato = EnumTipoContato.valueOf(contatoDTO.getTipoContato());
                    c.setContato(contatoDTO.getContato());
                    c.setCodigoArea(contatoDTO.getCodigoArea());
                    c.setTipoContato(tipoContato);
                    return c;
                })
                .map(ContatoDTO::new);
    }
    
    public Page<Contato> findAll(Long idPaciente, Pageable pageable){
        return contatoRepository.getContatosByIdPaciente(idPaciente, pageable);
    }
    
    public List<ContatoDTO> findAll(Long idPaciente){
        return contatoRepository.getAllContatosByPacienteId(idPaciente).stream().map(ContatoDTO::new).collect(Collectors.toList());
    }
    
    public Contato findContato(Long idContato){
        return contatoRepository.getContatoForCurrentUser(idContato);
    }

    public void deleteContato(Long idContato){
        contatoRepository.deleteByCurrentUser(idContato);
    }
}
