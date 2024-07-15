package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.model.entity.ApplicationDocument;
import org.klozevitz.model.entity.ApplicationPhoto;
import org.klozevitz.model.repositories.ApplicationDocumentRepository;
import org.klozevitz.model.repositories.ApplicationPhotoRepository;
import org.klozevitz.service.interfaces.FileService;
import org.klozevitz.utils.CryptoTool;
import org.springframework.stereotype.Service;

@Log4j
@Service
@RequiredArgsConstructor
public class FileServiceImplementation implements FileService {
    private final ApplicationDocumentRepository appDocRepo;
    private final ApplicationPhotoRepository appPhotoRepo;
    private final CryptoTool cryptoTool;

    @Override
    public ApplicationDocument getDocument(String hash) {
        Long id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appDocRepo.findById(id).orElse(null);
    }

    @Override
    public ApplicationPhoto getPhoto(String hash) {
        Long id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appPhotoRepo.findById(id).orElse(null);
    }
}
