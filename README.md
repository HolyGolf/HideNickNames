 [![logo](/imagies/6.png)](https://www.spigotmc.org/resources/hidenicknames.77039/)

# HideNickNames
This plugin allows you to hide and show player nicknames.

```
Commands:
/hnames - help command.
/hnames reload - reload plugin. (HideNicknames.switching permission)
/hnames on/off - Hide/Unhide nicknames. (HideNicknames.switching permission)
```

 ![hnames command](/imagies/1.png)
 
 ## /hnames on
 
![hnames on](/imagies/4.png)

```
Notice: When you are standing in the nether portal,
in a world where nickname hiding is enabled, then
your nickname is visible. Sorry, it was just the easiest
solution to the problem :) I'll fix it with updates.
```
 ## /hnames off
 
 ![hnames off](/imagies/3.png)
 
 ## You can also right-click on a player to see his nickname:
 
 ![click](/imagies/2.png)
 
 ## Config file:
 
```yml
#true - on || false - off
Hide_NameTags: true
#Enable/Disable plugin
Show_NameTags: true
#Show player NameTag on right click
NameTags_show: '&l&6-=[&9<player>&l&6]=-'
#<player> - player nickname
Permissons: true
# | HideNicknames.switching | Allows you to enable or disable the visibility of names for all (Enable or disable plugin) (Default "Op")
NoPermissionsMessage: '&cYou do not have permission to use this command!'
#Permission message

Enabled_Worlds:
  - world
  - world_nether
  - world_the_end

# List of worlds where enabled plugin.
```
