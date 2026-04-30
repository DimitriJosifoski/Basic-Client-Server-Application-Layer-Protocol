@echo off

echo === Testing v2 ===
start "Server" /b java AdditionServer_v2_Hw4 > data_v2_server_results_test.txt
timeout /t 3 /nobreak > nul
java AdditionClient_v2_Hw4 < data_v2.txt
timeout /t 2 /nobreak > nul
echo --- Server v2 output ---
type data_v2_server_results_test.txt

echo === Testing v3 ===
start "Server" /b java AdditionServer_v3_Hw4 > data_v3_server_results_test.txt
timeout /t 3 /nobreak > nul
java AdditionClient_v3_Hw4 < data_v3.txt
timeout /t 2 /nobreak > nul
echo --- Server v3 output ---
type data_v3_server_results_test.txt

echo === Testing v4 ===
start "Server" /b java AdditionServer_v4_Hw4 > data_v4_server_results_test.txt
timeout /t 3 /nobreak > nul
java AdditionClient_v4_Hw4 < data_v4.txt
timeout /t 2 /nobreak > nul
echo --- Server v4 output ---
type data_v4_server_results_test.txt

taskkill /F /IM java.exe > nul 2>&1
