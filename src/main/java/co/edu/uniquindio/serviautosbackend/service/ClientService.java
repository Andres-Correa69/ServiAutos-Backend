package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.dto.ClientCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ClientDTO;

import java.util.List;

public interface ClientService {
    ClientDTO create(ClientCreationDTO dto);

    ClientDTO getById(String id);

    List<ClientDTO> getAll();

    ClientDTO update(String id, ClientCreationDTO dto);

    void delete(String id);
}
