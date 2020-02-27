# D2File

Programa com o objetivo de fazer a cópia (vulgo backup) em massa de uma hierarquia de pastas usando o critério de última modificação.

No momento, configurado para reconhecer como arquivos antigos aqueles que foram modificados pela última vez antes de 01/07/2019

1) Reconhece o arquivo e caso esteja no parâmetro, é criada toda a hierarquia de pastas desde a raiz (onde foi a primeira leitura) na pasta Backup
2) Copia o arquivo para a pasta Backup
3) Deleta o arquivo do diretório original


Obs:
Pastas vazias ou sem arquivos antigos não são criadas no backup. Sendo assim, as pastas geradas pelo código são necessariamente as que contém arquivos dentro dos parâmetros


Ideias para o futuro:
- Facilitar a escolha do diretório a ser verificado
- Escolher o diretório de backup
- GUI