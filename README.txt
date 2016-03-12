CS 349 - A2 - Doodle

Colour/Stroke Palette:
 - colour palette has pre-set fixed colours
 - colour blocked indicated as "Current Colour" shows the current colour
 - button labeled "More Colours" opens up JColourChooser for more colour options
 - stroke button highlighted in light pink indicates currently selected stroke
 - the left-side panel including colour and stroke selection can be undocked by dragging on the indicated location on the menu to create a floating toolbar
 - if toolbar does not have enough space to display, added scrollbars allow for scrolling
 
Slider:
 - slider allows for stopping in between two ticks
 - stopping at a percentage between two ticks allow you to display the reflected percentage of the stroke
 - drawing again when the slider is in between two ticks will undo only the sections of the stroke that are not visible on the screen - the visible section will not be undone
 - if a line consists only of a point, the slider must stop at a tick as there are no valid percentage to represent the stroke
 - start button brings the tick to the start of the slider with nothing displayed on the screen
 - end button brings the tick to the end of the slider with every stroke displayed
 - play button plays back the stroke in real-time that the stroke was drawn at
 - while an animation is playing, incoming events will not be processed until after the completion of the animation
 - labels are displayed at the ticks of the slider to indicate the number of stroke

File Saving/Loading
 - if there are contents on the canvas that have not been saved, a prompt to save the file will be displayed when creating a new doodle, exiting the program, or loading from a file
 - when saving, if no extension is added and a specific file format is selected, an extension will be appended to the file name
 - if "All Files" is chosen, the program will differentiate file type based on the extension - no extension will result in an unsuccessful save
 - supported formats are Binary (.bin) and Text (.txt) files - unsupported formats or files will result in unsuccessful operation