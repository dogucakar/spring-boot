package org.digitalwallet.app.mapper;

import org.digitalwallet.app.dto.WalletDTO;
import org.digitalwallet.app.dto.request.WalletRequest;
import org.digitalwallet.app.repository.model.Wallet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletDTO toDto(Wallet wallet);
    List<WalletDTO> toDtoList(List<Wallet> wallets);
    Wallet toEntity(WalletRequest walletRequest);
}