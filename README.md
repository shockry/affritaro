# affritaro
Side-scrolling platformer game

This project started in 2014 to make a platformer game and it hasn't seen much progress since then, so I think sharing it with the public can help it make progress or help someone get started with their own game.

It uses LibGDX (1.0.1) and code from [this tutorial] (https://www.youtube.com/playlist?list=PL-2t7SM0vDfdYJ5Pq9vxeivblbZuFvGJK) as a basis, but adds up a lot to make it work more like a platformer game.

![screenshot] (http://i.imgur.com/DDDTXeT.png)

Although it it only have one level to demonstrate what it can do, it has most of the basic building blocks for a platformer game.

_**What's included:**_

* Player can move, jump climb and die
* Guns and bullets
* Jump springs
* Enemies (Can only die with gun shots)
* Ladders (Can only be climbed upwards :D)
* Collectable items
* Moving platforms (the ones that travel left and right while you ride them)
* Health bar
* Countdown falling blocks (countsdown and falls, you die if you're on top of it)
* Teleportation tunnels (one direction)
* Basic touch events support

_**What's missing:**_

* Good artwork (it's all placeholders now)
* Levels
* Finish line for levels
* Menu
* Score board
* What's left for touch events (like firing bullets and climbing ladders)
* Your own story (mine is a little ghost searching for its teddy bear, most probably you want to come up with another one)
* Anything else you can think of


__Creating levels__

Levels can be easily created with [Tiled map editor] (http://www.mapeditor.org/) and imported into the game.

Use the first level in `android/assets/res/maps/level1.tmx` inside the project folder as a reference for object types.


__Building and running__

This is a Gradle-based project, refer to [this] (https://github.com/libgdx/libgdx/wiki/Setting-up-your-Development-Environment-%28Eclipse%2C-Intellij-IDEA%2C-NetBeans%29) page in the LibGDX docs to setup your IDE and then import it as a Gradle project, [instructions can also be found here] (https://libgdx.badlogicgames.com/documentation.html)

<br>
MIT license
