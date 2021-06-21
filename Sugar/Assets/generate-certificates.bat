@echo off
@chcp 65001
@set PATH=%PATH%;".\Dependencies\openssl\bin"
@title Генерация сертификатов

set /p Domain=Домен или IP: 

openssl dhparam -out dh.pem 1024
openssl genrsa -out privkey.pem 2048
openssl req -new -sha256 -key privkey.pem -subj "/C=RU/ST=Reutov/O=Provincia/CN=%Domain%" -out site.csr
openssl x509 -req -in site.csr -CA CA.crt -CAkey CA.key -CAcreateserial -out cert.pem -days 365 -sha256
type CA.crt cert.pem > fullchain.pem
rem site.csr
@pause