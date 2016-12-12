# xml-mapper
A program I wrote to read in xml data and spit out a migration map

It makes the assumption that the xml document is organized as follows:

1. Document node
  2. Child 1
  3. Child 2
  4. ...
  5. Child N

It does not matter what the nodes are called, but the child nodes must have at least the following attributes:

* ID (id)
* Parent ID (pid)
* Title (title)
* URL path (UrlPath)

The program makes use of Java's DOM api in order to read and evaluate the xmlcontent data.

The final assumption is that, if no arguments are provided at the command line, the file containing the xml data will be called "webcontent.xml".

As for output, the program creates a treeview of all the pages represented in the xml content file. It then spits the tree out into a .csv file. This file was to be the migration map of the site, as the program was created to automate its production.
