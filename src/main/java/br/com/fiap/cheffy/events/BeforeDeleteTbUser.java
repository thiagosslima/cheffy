package br.com.fiap.cheffy.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@Getter
@AllArgsConstructor
public class BeforeDeleteTbUser {

    private UUID id;

}
