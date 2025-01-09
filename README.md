Dağıtık Abonelik Servisi

Bu proje, Dağıtık Abonelik Servisi ödevi kapsamında geliştirilmiştir. Ruby, Java ve Python kullanılarak bir HASUP (Hata-Tolere Abonelik Servisi Üyelik Protokolü) sistemi uygulanmıştır. Sistem hata toleransı, yük dengeleme ve dağıtık mimari prensipleri doğrultusunda tasarlanmıştır.
Projenin Özeti

Sistem aşağıdaki bileşenlerden oluşmaktadır:

Dağıtık Sunucular (Java):

Üç sunucu (Server1, Server2, Server3) birbirleriyle haberleşerek abonelik bilgilerini saklar ve yönetir.

Protobuf ile tanımlanmış nesneleri kullanır (Subscriber, Configuration, Capacity, Message).

Sunucular arası iletişim ve istemci/sunucu iletişimi için TCP soketleri kullanılmıştır.

İstemci (Ruby):

Protobuf kullanarak Subscriber nesnelerini sunuculara gönderir.

Admin istemciden gelen kapasite sorgularını iletir ve sonuçları işler.

Admin İstemci (Ruby):

Sunuculara başlatma (STRT) ve kapasite sorgulama (CPCTY) komutları gönderir.

Kapasite bilgilerini toplar ve Plotter uygulamasına iletir.

Plotter (Python):

Sunucuların kapasite durumlarını görselleştiren bir grafik oluşturur.

5 saniyelik aralıklarla güncellenen kapasite bilgilerini dinler ve işler.

Kullanım Talimatları

1. Sunucuların Başlatılması

Her sunucu için aşağıdaki komut formatını kullanarak Java uygulamasını çalıştırın:
java DistributedServer <serverId> <port>

Örneğin, Server1 için:java DistributedServer 1 5001

2. Admin İstemcisinin Çalıştırılması

Admin istemciyi aşağıdaki komut ile çalıştırın:ruby admin_client.rb
Admin istemci, dist_subs.conf dosyasını okuyarak sunucuları başlatır ve kapasite sorgularını yürütür.

3. Plotter Uygulamasının Çalıştırılması

Plotter uygulamasını çalıştırmak için:python plotter.py
Plotter, admin istemci tarafından gönderilen kapasite bilgilerini alır ve görselleştirir.

4. İstemci Uygulamasının Kullanımı

 İstemci uygulaması Protobuf kullanarak Subscriber nesneleri gönderir.

client.rb dosyasını çalıştırarak sunuculara bağlanabilirsiniz:ruby client.rb

Gereksinimler

Java 11 veya üzeri

Ruby 2.7 veya üzeri (gerekli gem'ler için bundle install komutunu çalıştırın)

Python 3.8 veya üzeri

Protobuf Compiler

Proje Yapısı
├── java_distributed_server
├── admin_client.rb
├── client.rb
├── plotter.py
├── dist_subs.conf
├── protos
│   ├── Capacity.proto
│   ├── Configuration.proto
│   ├── Message.proto
│   └── Subscriber.proto
└── README.md

Proje Geliştirme Süreci

Sunucuların geliştirilmesi:

Çok iş parçacıklı yapıya sahip sunucular tasarlandı.

Protobuf ile tanımlı nesneler kullanıldı.

Admin istemcisinin geliştirilmesi:

Sunucuları başlatma ve kapasitelerini sorgulama özellikleri eklendi.

Plotter uygulamasının geliştirilmesi:

Sunucuların kapasite bilgilerini grafiksel olarak gösteren bir Python uygulaması.

İstemci uygulamasının geliştirilmesi:

Subscriber nesnelerinin sunuculara gönderilmesi sağlandı.
