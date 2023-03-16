Swing Color Chooser
===================

This is a simple one-click color chooser with a popup, developed in 1999 for NetBeans and until a few years
ago, hosted on java.net.  Loosely based on a lovely color chooser from
[KPT Bryce](https://en.wikipedia.org/wiki/Bryce_(software)) from the 90s.

It is used in [Gephi](https://gephi.org/), the graph visualization tool,
[Imagine](https://github.com/timboudreau/imagine), an image editor, some
[NetBeans Platform](https://netbeans.apache.org/kb/docs/platform/) tutorials,
and probably other projects (if you want a shout-out, submit a [request](https://github.com/timboudreau/colorchooser/issues)).

The purpose is to make color-selection fast and easy - clicking the chooser pops
up a palette, which you drag to select a color in, and it closes when the mouse
button is released - so, unlike complicated color dialogs, color selection is a
single gesture.  Pressing combinations of modifier keys switches between a number
of palettes (and different palettes can be passed to the constructor).

<img src="https://github.com/timboudreau/colorchooser/blob/master/www/images/cc2.png?raw=true" alt="Screen shot"/>

The original web site for the project
[is here](https://rawcdn.githack.com/timboudreau/colorchooser/89ed33622599f31236cfaa8e1e8b7151b51503e4/www/index.html).

#### Backstory

This was literally some of the first Java code I ever wrote, in 1999, as an alternate color chooser
for NetBeans - a port of a Delphi component I had written in 1996 or so.  Sun later acquired NetBeans,
and we open sourced the code on the long-defunct (thanks, Oracle) `java.net`.

It is localized (what few strings there are) into English, Spanish, French, Dutch and German.

It has been used in various projects over the years, maintained (acquiring alpha palettes and clipboard
support, including copy/paste HTML `#`-prefixed hex colors, just a couple of years ago), and is
stable and solid - and to my knowledge there is nothing like it for easy, low-UI-footprint (it's
just a colored swatch, no dialogs to close) color selection in Java.
