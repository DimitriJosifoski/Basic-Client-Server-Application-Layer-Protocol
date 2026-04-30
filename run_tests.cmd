javac -Xlint -Xdiags:verbose  AdditionServer_v1_Hw4.java
javac -Xlint -Xdiags:verbose  AdditionClient_v1_Hw4.java
javac -Xlint -Xdiags:verbose  AdditionServer_v2_Hw4.java  
javac -Xlint -Xdiags:verbose  AdditionClient_v2_Hw4.java
javac -Xlint -Xdiags:verbose  AdditionServer_v3_Hw4.java
javac -Xlint -Xdiags:verbose  AdditionClient_v3_Hw4.java
javac -Xlint -Xdiags:verbose  AdditionServer_v4_Hw4.java
javac -Xlint -Xdiags:verbose  AdditionClient_v4_Hw4.java

start "Server" cmd /c java  AdditionServer_v1_Hw4  ^> .\data_v1_server_results@.txt
java  AdditionClient_v1_Hw4  < .\data_v1.txt  > data_v1_client_results@.txt

start "Server" cmd /c java  AdditionServer_v2_Hw4 ^> .\data_v2_server_results@.txt
java  AdditionClient_v2_Hw4  < .\data_v2.txt  > data_v2_client_results@.txt

start "Server" cmd /c java  AdditionServer_v3_Hw4 ^> .\data_v3_server_results@.txt
java  AdditionClient_v3_Hw4  < .\data_v3.txt  > data_v3_client_results@.txt

start "Server" cmd /c java  AdditionServer_v4_Hw4 ^> .\data_v4_server_results@.txt
java  AdditionClient_v4_Hw4  < .\data_v4.txt  > data_v4_client_results@.txt

taskkill /F /IM java.exe
pause
