## Dodgeball project
> Study project for my 'Concurrent programming' course at the University,
> practicing the relating tools and concepts in JAVA language.

The console application is a simulation of the dodgeball game in a room with 10 players.
The players (A ... J) and the ball (o) are separate `Thread`s and they can interact with each other bounded by a 5x5 room (`Room`),
through synchronization on the room and through synchronized collections (`Collection.synchronizedList()`).

![dodgeball (1)](https://github.com/tamaslaci/dodgeball/assets/173500621/6388b745-c75b-4d03-acdb-123f7ea30bad) ... ![dodgeball (2)](https://github.com/tamaslaci/dodgeball/assets/173500621/5e592086-8a6a-4746-a6a3-9d98c912a8aa) ... ![dodgeball (3)](https://github.com/tamaslaci/dodgeball/assets/173500621/d858c0d3-9c45-4b6a-89a6-620818c8f788) ... ![dodgeball (4)](https://github.com/tamaslaci/dodgeball/assets/173500621/e1949a01-2e42-477f-a285-889c915630ea) ... ![dodgeball (5)](https://github.com/tamaslaci/dodgeball/assets/173500621/175a9657-cd8d-48ca-92c5-12d6ccbe11d7)

The players can throw the ball in a random direction, away from themselves, if they are on an adjacent field of the room,
so then the ball in movement can knock out other players or stop at the side of the room.
After only one player remains the winner is announced and the program terminates:

![dodgeball (6)](https://github.com/tamaslaci/dodgeball/assets/173500621/119d0835-0aa2-4aca-9356-e9d6d2c0e3d8)





