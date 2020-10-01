# SOA-CC-Task4

- Root URL: http://localhost:8080/webshopREST/
- Ohjelma lataa käynnistyessään datan `shopdata.json` -tiedostosta muistiin. Tätä tiedostoa ei ole tarkoituksenmukaista muokata ajon aikana, vaan ohjelma käynnistyy aina samasta lähtötilanteesta kun se ajetaan uudestaan.
- Postman tiimin invite-linkki: https://app.getpostman.com/join-team?invite_code=d96317ac99e794b98320e69b8213f1c2&ws=7de7b5e9-af7e-45a6-88a7-970a7969f1bb

### Devlog

#### 1.10. (Jukka)
- Tehty `User`-tyyppi, jolla kaikki perusoperaatiot
- Käyttäjän Rooli käyttää `Role`-enumeraatiota, jonka arvoja on `ADMIN` tai `USER`.
  - Sen avulla avulla voidaan tulevaisuudessa testata oikeuksia ilman merkkijonoja tyyliin `if (user.getRoles().contains(Role.ADMIN))`
- Käyttäjää ei voida luoda olemassa olevalla ID:llä, eikä virheellisellä IDllä. Käyttäjän ID:tä ei voida muuttaa käyttäjää muokatessa.
  - Nämä kaikki virheviestit käyttävät `InvalidInputException`ia (BAD_REQUEST)

### Import

Jos kloonaat projektin aiempaan Eclipe workspaceen, saat sen näkyviin Eclipsessä Importin avulla:

![Import](https://i.imgur.com/NqOYnHn.png)

### Java-versio

Todettu toimivaksi ainakin uusimmalla JDK -versiolla `14.0.2`

![JDK -versio](https://i.imgur.com/NZkKSw2.png)