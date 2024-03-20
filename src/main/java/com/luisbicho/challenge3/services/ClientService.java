package com.luisbicho.challenge3.services;

import com.luisbicho.challenge3.dto.ClientDTO;
import com.luisbicho.challenge3.entities.Client;
import com.luisbicho.challenge3.repositories.ClientRepository;
import com.luisbicho.challenge3.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable){
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients.map(x->new ClientDTO(x));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Optional<Client> client = clientRepository.findById(id);
        return new ClientDTO(client.orElseThrow(()->new ResourceNotFoundException("ID NOT FOUND")));
    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO){
        Client client = new Client();
        updateData(client,clientDTO);
        client = clientRepository.save(client);
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO update(Long id,ClientDTO clientDTO){
        try {
            Client client = clientRepository.getReferenceById(id);
            updateData(client, clientDTO);
            client = clientRepository.save(client);
            return new ClientDTO(client);
        }
        catch(EntityNotFoundException e){
            throw new ResourceNotFoundException("ID NOT FOUND");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteById(Long id){
        if(!clientRepository.existsById(id)){
            throw new ResourceNotFoundException("ID NOT FOUND");
        }
        clientRepository.deleteById(id);
    }

    private void updateData(Client client, ClientDTO clientDTO) {
        client.setName(clientDTO.getName());
        client.setCpf(clientDTO.getCpf());
        client.setIncome(clientDTO.getIncome());
        client.setBirthDate(clientDTO.getBirthDate());
        client.setChildren(clientDTO.getChildren());
    }

}
