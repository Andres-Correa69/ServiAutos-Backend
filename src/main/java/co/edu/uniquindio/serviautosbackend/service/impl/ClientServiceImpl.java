package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.domain.models.Client;
import co.edu.uniquindio.serviautosbackend.dto.ClientCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ClientDTO;
import co.edu.uniquindio.serviautosbackend.repository.ClientRepository;
import co.edu.uniquindio.serviautosbackend.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ClientDTO create(ClientCreationDTO dto) {
        Client client = new Client();
        client.setName(dto.name());
        client.setLastName(dto.lastName());
        client.setDocument(dto.document());
        client.setEmail(dto.email());
        client.setAddress(dto.address());
        client.setPhone(dto.phone());

        return mapToDTO(clientRepository.save(client));
    }

    @Override
    public ClientDTO getById(String id) {
        return clientRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public List<ClientDTO> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ClientDTO update(String id, ClientCreationDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        client.setName(dto.name());
        client.setLastName(dto.lastName());
        client.setDocument(dto.document());
        client.setEmail(dto.email());
        client.setAddress(dto.address());
        client.setPhone(dto.phone());

        return mapToDTO(clientRepository.save(client));
    }

    @Override
    public void delete(String id) {
        clientRepository.deleteById(id);
    }

    private ClientDTO mapToDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getLastName(),
                client.getDocument(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone()
        );
    }
}
