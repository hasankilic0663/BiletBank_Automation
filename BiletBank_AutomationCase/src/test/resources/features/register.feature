Feature: Yeni Acente Kaydi - Form Verification & Validation


  Scenario: Zorunlu alanlar bos birakildiginda sistem ilgili hata mesajlarini gostermelidir
    Given Kullanıcı acente Ana Sayfasina gidilir
    When Yeni Acente Uyelik linkine tiklanilip yeni sekmeye gecilir
    And Sozlesmeyi kabul eder ve devam eder
    Then Bos formda zorunlu alan validasyonlari gorunur

  Scenario: Tum alanlar dogru doldurulunca kayit basarili olmali
    Given Kullanıcı acente Ana Sayfasina gidilir
    When Yeni Acente Uyelik linkine tiklanilip yeni sekmeye gecilir
    And Sozlesmeyi kabul eder ve devam eder
    When Tum zorunlu alanlari kurallara uygun doldurur
    And Kaydet butonuna basar
    Then Islem basarili mesaji gorunur



  Scenario: Bos form kaydedilmeye calisildiginda zorunlu alan hatalari gorunmeli
    Given Kullanıcı acente Ana Sayfasina gidilir
    When Yeni Acente Uyelik linkine tiklanilip yeni sekmeye gecilir
    And Sozlesmeyi kabul eder ve devam eder
    And Kaydet butonuna basar
    Then Zorunlu alan hata mesajlari goruntulenmelidir

  Scenario: Tum alanlar gecerli veri ile dolduruldugunda kayit basarili olmali
    Given Kullanıcı acente Ana Sayfasina gidilir
    When Yeni Acente Uyelik linkine tiklanilip yeni sekmeye gecilir
    And Sozlesmeyi kabul eder ve devam eder
    And Tum zorunlu alanlari kurallara uygun doldurur
    And Kaydet butonuna basar
    Then Islem basarili mesaji gorunur

  Scenario: Özel Karakter girmeden Gecersiz sifre formatinda hata alinmali
    Given Kayit formu aciktir
    When Sifre özel karakter içermeden veri girilir
    And Kaydet butonuna basar
    Then Sifre karakter validasyon hatasi gorulmelmelidir

  Scenario: Telefon alanina harf girildiginde hata alinmali
    Given Kayit formu aciktir
    When Telefon alanina harf karakter girilir
    And Kaydet butonuna basar
    Then Telefon icin validation hatasi gorulmelidir

  Scenario: Sifre minimum karakterden kucuk girildiginde hata alinmali
    Given Kayit formu aciktir
    When Sifre 5 karakter olarak girilir
    And Kaydet butonuna basar
    Then Sifre minimum karakter hatasi gorulmelidir

  Scenario: Acente adi minimum sinir degerin altında kabul edilmemeli
    Given Kayit formu aciktir
    When Acente adi minimum karakter sinirinda girilir
    And Kaydet butonuna basar
    Then Islem basarisiz mesaji gorunur

  Scenario: Kullanilmiş acente ismi ile kayit sirasinda hata vermesi
    Given Kayit formu aciktir
    When Kullanilmis acente adi girilir
    And Kaydet butonuna basar
    Then Acente adi kullanilmis hatasi gorulmelidir

  Scenario: Email alani bos birakildiginda hata alinmali
    Given Kayit formu aciktir
    When Email alani bos birakilir
    And Kaydet butonuna basar
    Then Email zorunlu alan hatasi gorulmelidir

  Scenario: Adres Detayı alanı özel karakter girme validasyon kontrolü
    Given Kayit formu aciktir
    When Adres Detayı alanına ozel karakterler girilir
    And Kaydet butonuna basar
    Then Adres detay alani validasyon hatasi görülür



  Scenario: Yol tarifi alanına özel karakterler girme sonucu validasyon hatasi kontrolü
    Given Kayit formu aciktir
    When Yol tarifi  ozel karakterler girilir
    And Kaydet butonuna basar
    Then Validasyon hatasi gorulur

  Scenario: IBAN bilgileri eksik girilerek kayıt etme
    Given Kayit formu aciktir
    When IBAN eksik karakterle girilir
    And Kaydet butonuna basar
    Then IBAN eksik karakterle girildi hata mesaji gorulmeli



  Scenario: Aynı email ile tekrar kayit yapilamamali
    Given Daha once kayitli bir email ile form doldurulur
    And Kaydet butonuna basar
    Then Email zaten kayitli hatasi gorulmelidir