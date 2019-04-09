#include <iostream>
#include <winsock2.h>
 
using namespace std;

const int MAX_SZ = 1e4;
 
struct Player {
    int r, c, s; //r and c is the player's current position, while s is 1 if the player is shooting and 0 otherwise.
} p1, p2;

int main()
{
    WSADATA WSAData;
 
    SOCKET server, client1, client2;
 
    SOCKADDR_IN serverAddr, clientAddr;
 
    WSAStartup(MAKEWORD(2,0), &WSAData);
    server = socket(AF_INET, SOCK_STREAM, 0);
 
    serverAddr.sin_addr.s_addr = INADDR_ANY;
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(5555);
 
    bind(server, (SOCKADDR *)&serverAddr, sizeof(serverAddr));
    listen(server, 0);
 
    cout << "Listening for incoming connections..." << endl;
 
    char* buffer;
    string message;
    int clientAddrSize = sizeof(clientAddr);
    if ((client1 = accept(server, (SOCKADDR *)&clientAddr, &clientAddrSize)) != INVALID_SOCKET) {
        cout << "Client connected!" << endl;
        buffer = "1";
        send(client1, buffer, sizeof(buffer), 0);
        p1 = {0, 0, 0};
    }
    if ((client2 = accept(server, (SOCKADDR *)&clientAddr, &clientAddrSize)) != INVALID_SOCKET) {
        cout << "Client connected!" << endl;
        buffer = "2";
        send(client2, buffer, sizeof(buffer), 0);
        p2 = {10, 5, 0};
    }

    while (true) {
        //server updates clients with the positions of both players in the form "r1 c1 s1|r2 c2 s2"
        message = to_string(p1.r) + " " + to_string(p1.c) + " " + to_string(p1.s) + "|" + to_string(p2.r) + " " + to_string(p2.c) + " " + to_string(p2.s);
        send(client1, buffer, sizeof(buffer), 0);
        send(client2, buffer, sizeof(buffer), 0);

        //client updates server with their players current state in the form of "r c s"
        recv(client1, buffer, sizeof(buffer), 0);
        p1.r = stoi(strtok(buffer, " "));
        p1.c = stoi(strtok(NULL, " "));
        p1.s = stoi(strtok(NULL, " "));

        recv(client2, buffer, sizeof(buffer), 0);
        p2.r = stoi(strtok(buffer, " "));
        p2.c = stoi(strtok(NULL, " "));
        p2.s = stoi(strtok(NULL, " "));
    }
}