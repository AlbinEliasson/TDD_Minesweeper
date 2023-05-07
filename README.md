# DT042G Project: Minesweeper

## Table of contents

1. [Group 9](#section-1)
2. [Environment & Tools](#section-2)
3. [Project description](#section-3)
4. [Execution instructions](#section-4)
5. [Purpose](#section-5)
6. [Procedures](#section-6)
7. [Discussion](#section-7)

## 1. Group 9: <a name="section-1"></a>

| Name       | Albin Eliasson           | Martin K. Herkules          |
|------------|--------------------------|-----------------------------|
| Student id | alel2104                 | makr1906                    |
| E-mail     | alel2104@student.miun.se | makr1906@student.miun.se    |

## 2. Environment & tools <a name="section-2"></a>

| Tool             | alel2104             | makr1906                    |
|------------------|----------------------|-----------------------------|
| Operating system | Windows 11           | Windows 10                  |
| IDE              | IntelliJ 2022.3.3    | IntelliJ 2021.3.3           |
| Java             | OpenJDK 18.0.1.1 (*) | OpenJDK 18 (2022-03-22) (*) |
| Maven            | Apache Maven 3.8.5   | Apache Maven 3.8.5          |
| GIT              | git 2.33.1.windows.1 | git 2.33.0.windows.2        |

(*) But the project is configured to compile using JDK 17.

## 3. Project description <a name="section-3"></a>
The idea behind this project was to create a custom implementation of the classic game Minesweeper. The Minesweeper game is based on having a board of N x N squares. Each square can be either a mine or a "value" square. With the goal of the game being to click all "value" squares, without clicking any of the mines; the catch being that all squares are hidden at the start of the game.

When the user clicks on the first square, it should reveal a couple of initial "value" squares. Provided the "values" of the revealed squares, the user must figure out where all the mines on the board are. This can be done since the actual value of "value" squares represent the number of neighboring mines. So the goal is to reveal all value squares, without clicking any mines; and optionally to right-click the mine squares to flag them to indicate that you know they are mines.

## 4. Execution instructions <a name="section-4"></a>
To run the game the jar file must first be built. For this maven has been configured to do this automatically when the `mvn clean verify` command is run in the base directory of the project. This should produce an executable jar file in the `./target` directory which can then be executed. However, depending on the underlying environment there seems to be a lot of inconsistencies with how the file wants to be executed. However, with all tested environments execution from the terminal always works; resulting in the final instructions being:

1. Download the GUI repository.
2. Run the command `mvn clean verify` in the base directory.
3. Navigate to the `.jar` file in the `/target` folder.
4. Execute the `.jar` file using the command `java -jar "path/to/filename.jar"`
5. Play the game.

## 5. Purpose <a name="section-5"></a>
The goal of this project is to create an application which is a capable implementation of the classic Microsoft game Minesweeper. The application should implement a Swing GUI as the medium for user interaction with the game. The GUI should allow for all of the standard Minesweeper actions; viewing the board, clicking squares to reveal them, and flagging squares by right-clicking them. The game should be lost if a mine is clicked, and if all non-mine squares are revealed the game is won; and the user can either exit the game or restart.

The general structure of the project implementation should also follow the MVC architectural pattern. With the game functionality being separated into data handling (model), user interaction (view), a communications layer (controller); and the Observer pattern being used to trigger events between the elements.

Lastly the project should also follow the principles of TDD with thorough unit-testing being implemented before each part of the functionality.

## 6. Procedures <a name="section-6"></a>

### 6.1. Testing procedures
To start with the full project as followed the principles of test driven development, or TDD. The basic premise of TDD is to split the implementation in to individual features, and the split each feature into parts that are as small as possible. Then before implementing each part, writing a unit-test for this test which ensures that the part of the feature functions correctly.

The basic steps used for applying TDD in this project can be summarized as:

1. **Define the requirements for a feature:** As the project was relatively large, it had to be split into sections which could be defined as tasks. Then for each task specific requirements were defined and documented in the Trello board which were then used as part of basis for determining completion criteria.
2. **Split feature into small parts:** Then when implementing a feature, it was again split into smaller parts. The aim of these parts were to make them as small as possible to allow for very specific testing. Each small part was defined by creating a specific test case for it.
3. **Write a failing test for part:** Then a test case was written for the part. The test was implemented first with the intention of it failing. Then the test case would serve two purposes; first it would serve as an indicator that the part had been successfully implemented. Then later it provides test coverage for the full feature which is useful for ensuring that future changes don't break the application.
4. **Write a minimal amount of code to pass test:** Then after writing the test, and implementation was created for the subject of the test. This first implementation always had the base goal of making the test pass, with very little thought put into the "beauty" of the implementation. As the purpose of this stage is to just create a working implementation, and then refine it in a later step.
5. **Run the test:** Then once the base implementation was created the test case was ran to ensure that the implementation is valid. Then together with this test all other tests were also ran, to ensure that the new code doesn't break any of the previous code.
6. **Refactor:** Then the implemenation for the part was refined to make it better. This could range from simply fixing spacing; all the way to completely refactoring a method, to incorporate the new functionality more efficiently.
6. **Repeat:** Lastly, this the steps 4-6 were repeated for each part of a feature until all the requirements were covered by tests, and fully implemented.

This describes most of the process of following TDD and generally implementing all the code for the implementation. However, there are a few additional details which may be of relevance. For instance there a few scenarios where there was need to test abstract classes or interfaces.

The reason being that there were instances where non-instantiable elements provided default implementations of methods. This meant that these methods needed to be tested. This could in theory be done by testing their concrete implementations, but this has a problem. Since methods can be overridden in sub-classes, it means that the default implementations could loose test coverage which is not acceptable. Therefore there was a need for a way to create instances of there elements.

There are additional frameworks which can do this, or at least emulate it, but of the tested methods none of the provided the functionality needed. One such example is *Mockito* which provides a framework for creating a "mock" of an abstract class or an interface. This mock then acts as a pseudo-instance of the element, upon which you can call its method and expect behavior. But the problem in this case is that the created "mock" isn't actually a real instance, it only pretends to be one.

While it does provide functionality for calling the real methods of the element, the element is not a real instance; which means that it does not have its internal variables. So if the method you are testing must access an internal variable, it will not work. There are workarounds for this with *Mockito* specifically, but they require extra work, with the end result of compromising the integrity of the test.

Therefore in the end the chosen solution was to just have specific concrete implementations for each abstract class and interface which needed to be tested. These concrete implementations are just empty classes which extend or implement the element to be tested. And abstract methods which need to be implemented are just empty. The reason being that the subject of the tests are all default implementations of the superclass/interface, which means that the implementations in the concrete implementations are irrelevant since they are not to be tested.

The advantage of this approach being that no extra framework is needed; and it can be ensured that the default implementations will not be overridden in the tests, so the test integrity can be ensured.

Lastly, the testing makes use of an additional testing library. The project uses TDD for all of its tests, following its structure. But to expand upon this is makes use of the `Mockito` framework. This framework provides a lot of functionality, but in this case only one part is relevant. Mockito allows you to create "spy" objects, which are instances of classes which are monitored by Mockito. You can then access information about what the instance is doing, outside of what is normally accessible.

In this project this was used when there was need to test that a method called another method. Such as under X circumstance method Y is called. It also allows for testing the number of times a method is called. Which means you can test the opposite, under circumstance X method Y is called twice, or method Z is never called. Lastly, it also allows you to access the parameters with which a method was called, which means you can ensure that method X calls method Y with correct parameters.

As mentioned Mockito provides a lot of additional functionality, but this is the only functionality which was needed and utilized in this project. An example of using mockito for ensuring that the model actually triggers a win event:

```Java
@Test
public void test_GameModel_Example() {
    ...

    // Tests that the GameModel.pushWinEvent() method was 
    // called exactly once.
    Mockito.verify(model, Mockito.times(1)).pushWinEvent();

    ...
}
```

### 6.2. Observer
The first part of the application that was created was an implementation of the observer pattern. The observer pattern is a structure which allows certain objects to attach themselves to others, and subscribe to events which they may cause. For the Minesweeper game, this will be used in conjunction with the MVC pattern to pass information between the model, view and controller.

The observer implementation is split into two parts, an abstract class `Observed` and an interface `Observer`. The `Observed` interface will be implemented by classes which will be generating events. The observed class provides three methods: `attachObserver`, `detachObserver` and `pushEvent`. The first two are used to attach and detach observer objects from the subject. To keep track of which observer object to push events to; each observed object keeps a list of references to observers. The `attachObserver` method allows a observer to be attached to receive events, and the `detachObserver` allows observers to be removed.

Then lastly, the `pushEvent` method is what is used to actually send events from the observed object to all attached observers. The method takes two parameters, one is an identifier for what event has occured; and another optional for providing event specific information. The additional information is in the form of a list of coordinates. The reason being is that all events relevant to the minesweeper game will either have no additional information; or be related to one or more specific squares on the board. Therefore it is sufficient to allow a list of square locations with each event.

Generally you wouldn't need to test abstract classes or interfaces, since they are not concrete implementations. However, in this case there are reasons to do so, which is that the `Observed` abstract class does provide default implementations of each method. This means that they have to be tested; and because these methods could be overridden, you shouldn't rely on just testing the concrete implementations, since this could lead to missing a bug in the default implementation; if the method is overridden in all concrete implementations.

As mentioned in the testing procedures section, to test the default implementations, concrete implementations specifically for testing were created. These classes are only used to test the default implemented methods, and therefore allow access to testing methods which would otherwise be inaccessible.

For example for testing the `Observed.attachObserver` method, the `TestingConcreteObserved` and `TestingConcreteObserver` were used. The `attachObserver` method has a default implementation, which means it needs to be tested to ensure that it works as expected.

### 6.3. MVC
The implementation is structured in accordance with the MVC architectural pattern, as defined in the project description. As the pattern dictates the project is split into three main component types: model, view and controllers. This is done in layers, at the top there are three abstract superclasses; one for each type: `Model`, `View` and `Controller`. These base classes are used as the superclasses of any concrete MVC implementations, which currently are: `GameModel`, `GameView`, and `GameController`.

These components follow the basic communication structure defined by the MVC pattern; where the model is responsible for data handling, the view for interacting with the user and displaying information, and lastly the controller directs communications between them. To achieve this, the observer pattern is used in conjunction with the MVC pattern. The model and view implement the `Observed` interface and the controller implements the `Observer` interface. Then when information needs to be transferred, the model and view can generate events, which are handled by the controller.

#### 6.3.1. Model superclass
The `Model` class acts as the superclass for all concrete model implementations in the project. It defines all the public methods which are needed for communication between the model and controller. This includes all getters and setters for information which is needed to display information in the view. But also methods for pushing specific events which can be caused by circumstances determined by the model.

To start with as mentioned the `Model` class implements the `Observed` interface which means that it is capable of producing events. There are a total of three event types which the model can cause:

* **Reveal squares event:** When the model receives a notified (by the controller) that the user has clicked a square, it calculates which squares are to be revealed. It then provides a list of square to be revealed in the form of an event.
* **Mine hit event:** When the model receives a notified (by the controller) that the user has clicked a square, if it is a square the model pushes a mine hit event to indicate that the game is over.
* **Win event:** When the model receives a notified (by the controller) that the user has clicked a square, it calculates which squares are to be revealed. Then it checks is if all non-mine squares have now been revealed. This is the win condition for the Minesweeper game, so if it detects this it pushes a win event to indicate that the user has finished the game.

The model also defines abstract methods for all interactions which the controller will need to have directly with the model. This includes getters for information about squares. As well as setters/triggers for causing the model to take action. An example is the `Model.selectSquare` method. This method is used by the controller to inform the model that the user has "clicked" a square.

Lastly, the `Model` class also provides helper methods for pushing each of the above event types. These helper methods are simple wrappers around the `Observed.pushEvent` class to make it easy to push specific event types, without having to provide a specific event id each time.

#### 6.3.2. View superclass
The `View` class acts as the super class for all concrete view implementations. It defines abstract methods for each interaction the controller must be able to do with the view. This mainly includes setters for square information, such as setting the value of a square when it is revealed; and propagating win/loss conditions produced by the model.

The view is capable of producing three types of events, each corresponding to a specific user interaction:

* **Select square event:** When the user clicks on a un-revealed square, the view triggers a select square event with the location of the clicked square. This event is what is used by the controller to tell the model to calculate which squares to reveal.
* **Flag square event:** Then a user right clicks on a square, the view triggers a flag event to put a flag on the hidden square, which prevents it from being accidentally clicked.
* **Reset game event:** If the user wants to restart the game, the view produces a reset game event. The reset event is pushed to the controller which resets the view and model.

Lastly, the `View` class also provides helper methods for pushing these specific event types, like the model. It provides one method for each of the above event types, which makes it nicer when actually calling these methods, as you don't have to provide an event id each call.

#### 6.3.3. Controller superclass
The `Controller` class acts as the super class for all concrete controller classes. It implements the `Observer` interface and defines abstract methods for each of the event types which the model and view can produce. Along with these abstract methods and the implementation of the observer interface, it provides a default implementation for the `handleEvent` method. The implementation takes the general event provided by the `handleEvent` method and directs the incoming event to the appropriate event handler method. The advantage being that the concrete implementations do not have to deal with sorting through events; instead each event handler method will be triggered automatically when appropriate.

### 6.4. Model implementation

#### 6.4.1. Square information storage
As defined by the MVC pattern the model is responsible for tracking and storing all information related to the state of the game; in this case the state of each square on the board. For each square there are three pieces of information which needs to be stored: whether the square is revealed, whether the square is flagged, and whether the square is a mine. Each non-mine square also needs a "value", however, this value is just the number of neighboring mines, which means it is better calculated on the fly rather than stored.

To store information about each square the class `BackingSquare` is used. This class is a simple data container with three booleans (and accompanying getters and setters): `revealed`, `flagged`, and `mine`. Each square on displayed on the board has a corresponding `BackingSquare` instance in the model. Inside the model, these squares are then stored in a two-dimensional list. The instances can then be accessed using the coordinates of the square, by indexing into the two-dimensional list.

#### 6.4.2. Square generation
Along with being responsible for tracking and storing square information, the model is also responsible for performing data manipulation and, in this case, data generation. The Minesweeper game is based upon the user figuring out the locations of hidden mines on a board. This means that the locations of the mines has to be generated. There are many ways to achieve this, there are established algorithms for generating boards with specific difficulty. But in accordance with original version created by Microsoft, this implementation randomly generates the mines, with one exception which will be discussed later.

The generation of squares is handled by an internal method in the `GameModel` class, `generateSquares`. This method is called whenever the game is started or restarted. The method is responsible for initializing the backing storage of `BackingSquare` objects and for setting their initial values. To do this the `GameModel` takes in a board size value in its construction. This value determines the number of squares on the board. The generation starts by initializing the two-dimensional list for storing the backing squares.

Then the to create each of the squares it iterates over each possible square location, which is (0 - board size, 0 - board size), which works since the backing storage is indexed using square locations in a grid:

```Java
// The backing square are stored in a two-dimensional list
List<List<BackingSquare>> _board;

// Each square is identified by (x, y) coordinates
int x;
int y;

// To access a specific square the coordinates are used
// to index into the two-dimensional list.
_board.get(x).get(y);
```

Then for each possible square location, as determined by the board size, a new `BackingSquare` object is created. Then for each square, whether it is a mine is randomized using a predefined percentage chance. This chance is hard-coded to be 15%. So for each square a random number is generated, if it is below the 15% threshold, the square is a mine; otherwise it is a normal "value" square.

However, there is a problem with the randomization approach. Since the mine locations are completely random, it is possible for the user to hit a mine on the first click, when they have no chance of knowing. This is of course a terrible user experience. To mitigate this, there is a "safety" built into the generation process.

The generation method takes in a parameter "first location". This parameter contains the coordinates of the first square which was clicked by the user in the GUI. Using this information, it can be ensured that the first square is not a mine. This is done after the mine randomization. The provided "first location" is checked to see if it is a mine, and if it is it is unset. Then the same is done for each of its neighboring squares.

This results in a guaranteed set of squares which are not mines at the first location the user clicks (2x2 in a corner, 2x3 along an edge, and 3x3 otherwise). This free space prevents the "instant loss", and also provides the user with sufficient vision to be able to solve the game further. Since, a few visible square are necessary to figure out where the mines are.

However, this approach does require that the generation logic knows which square was first clicked; in other words, the board must be generated after the user has clicked the first square. This seems weird, but since the model is ultimately responsible for updating the board when the user clicks a square, it is not a problem. All the model has to do is add a check when its `selectSquare` method is triggered. Before it performs its logic, it checks if the board has been generated, and if not it calls the generation logic, with the provided location as the first location. This results in the generation logic having access to the first location.

#### 6.4.3. Select square
Then we have the `selectSquare` method. This method is what the controller calls when it receives a "select square" event from the view. The `selectSquare` method is responsible for figuring out what happens when a user clicks on a hidden square. The select square method takes a location as a parameter, which corresponds to the square which was clicked in the GUI.

First it checks whether the board has been generated. If it doesn't exist, it means that the provided location is the "first location clicked". And if so it is provided to the generation method.

It then checks if the square at the location is currently revealed or flagged. If so, the method should do nothing, since a reveled square can't be revealed again; and a flagged square is protected from being selected.

Then it checks if the square is a mine. If it is, it means that the user has clicked a mine and the game is over. The model then pushes a "mine hit" event; which is handled by the controller.

But if the square is not a mine, revealed or flagged; it must be a "value" square. The value of squares is not provided by the select square method, instead that is available to the controller via a getter method. This means that at this point, the job of the `selectSquares` method is to calculate which squares are to be revealed.

Of course the "clicked square" is to be revealed, but Minesweeper also as an additional rule set which causes certain other squares to be revealed. If the "clicked" square has value zero, all its neighbors should also be revealed. Also, if any of the neighbors of the square are zero-value, it and its neighbors should also be revealed. This effect is then repeated recursively, for all zero-value square connected to the "clicked location".

This results in zero-value squares automatically being revealed, since a zero value square means that none of its neighbors are mines it just a small user experience improvement so they don't have to manually select all of the "obvious not mine" squares.

But to achieve this, the provided location, and each of its neighbors must be checked recursively. To do this another internal method with an obnoxious name is used `findAndRevealZeroValueNeighbors`. This method takes in a location and checks if the provided location is zero. If it is adds all the neighbors of the location to the return list. It also recurses into itself for each neighbor and appends the results of these calls to its own return. This results in all squares connected by zero-value square being included in the result.

The `selectSquare` method then takes the returned locations and provides them in a "reveal squares" event to allow the controller to update the view. But before returning a final check is done. The model checks if the win condition for the game has been achieved. In the game of Minesweeper, the goal is to reveal all squares which are not mines; without clicking any squares.

And since a set of squares has just been revealed, it must check if this has been achieved. To do this, the model keeps track of the number of mines and revealed squares on the board. The number of mines is countered in the `generateSquares` method and is stored as class properties along with the revealed count. The `selectSquare` method updates the reveal count every time it reveals a set of squares. Then all it has to do is check if the number of revealed squares plus the number of mines is equal to the total number of squares. If so, the game has been won, and a "win" event is pushed.

#### 6.4.4. Reset
The model also provides functionality for resetting the game, essentially restarting it by deleting the previous board. In the current implementation this functionality is used by the controller when the user presses the "Restart game" button in the GUI. The method is implemented very simply. Since the board is generated "as needed" when the first square is clicked, the `selectSquare` method checks if the board is null, and if so generates it. So all the reset method needs to do is set the "board storage" list to null, deleting the board state; and causing the board to automatically be regenerated when needed.

#### 6.4.5. Getters/setters, reset
The model also provides access methods for all relevant data which the controller must be able to access. The access methods themselves are defined in the `Model` superclass, but are abstract and the implementations are provided by the `GameController` implementation. The first three `isFlagged`, `isMine` and `isRevealed` are simple pass through getters for values stored in the `BackingSquare` two-dimensional array.

Then the `setSquareFlag` is a setter for whether a square is flagged. This method is used by the controller when it receives a "flag square" event to update the model. This method is also just a pass through method for setting the "flagged" value of the corresponding `BackingSquare`.

Then lastly there is the `getSquareValue` method which is used by the controller to get the "value" of a square. The controller needs this information when revealing a square. When a "value" square is revealed, its number should be shown to the user to indicate how many mines are neighboring it. This value is not stored by the model, and is instead calculated. It takes in a location, and counts the number of neighboring "mine squares", and returns the count.

### 6.5. View implementation
As defined by the MVC pattern the view is responsible for interacting with the user and displaying information; which in this project includes a Swing GUI with components to display the game board containing the squares of the game and a menu in which the user can view the current "game state" (playing state: "Minesweeper", game over state: "Game over!" and the win state: "You win!") as well as interactive buttons to restart the game and quit the game.

#### 6.5.1. Square GUI component
As each square in the game board is to be clickable and display information depending on the current state of the square, the class `Square` is utilized. This class which extends the generic lightweight container `JPanel` utilizes an enum with the three states the square can posses: hidden, flagged or value. The hidden state, which is the initial state for every square marks that the square is not yet revealed or flagged.
The flagged state marks the square to be flagged and the value state marks the square to be revealed. To visually display the states a `JLabel` is utilized to hold either a text or an image icon.

The component utilizes a get method for accessing the squares current state and set methods for setting the current state of the square as well as displaying the state for the user. The `setHidden()` method sets the current state of the square to hidden and to visually display the state by making sure the `JLabel` has no text or icon image set. The method also makes sure the initial set background color/border of the square is set back to light gray with a Bevel Border.

The `setFlagged()` method sets the current state to flagged and to visually display the flagged state, utilizes the `JLabel` to set an icon image of a red flag from the resources' folder. To read in the icon image and to scale it properly, the class `ImageIO` with the static `read()` and `getScaledInstance()` methods was utilized.

The `setValue(final int value)` method sets the current state to value and this method is utilized to visually display the provided value of the square (a number from 0-8). The method utilizes the `JLabel` to set the value as text and depending on the value number provided, sets a unique text color for each number. As Minesweeper contains "blank space" (squares with no value) the method makes sure 0 number values are not set as text in the `JLabel`.
Other than setting the state and displaying the number value of the square, the background color/border is changed to gray and a line border to aid the revealed square visually.

The `setMine()` method also sets the current state to value as the square is revealed. The method, like the `setFlagged()` method reads in an image from resources' which this time depicts a mine and sets it as an image icon in the `JLabel`. To aid the revealed square visually the background color/border is changed to red with a line border.

#### 6.5.2. Board
To create and display the interactive board containing the squares in the `GameView` class which is the concrete implementation of the super class view, the `initializeBoard()` method initializes a `JPanel` with a preferred size, utilizing the set width and game height constants as well as a grid layout of the provided board size from `GameView` constructor as rows and columns.
Utilizing the board size, a for-loop is created to create new `Square` instances and add them to the `JPanel`.

To make sure the user can interact with each square in the board, before adding the new squares to the `JPanel`, a mouse listener with a `MouseAdapter` is added to override the `mousePressed(final MouseEvent mouseEvent)` method. As the user is to be able to reveal a square or flag a square, a check is made for left mouse button click (reveal square) and the right mouse button click (flag square).
To reveal or flag a square the point location of the square in the board needs to be calculated, which the `calculateSquareBoardPosition()` method is utilized for. The method returns a new `Point` location utilizing the index of the square in the board modulo the board size as x-position and the index divided with the board size as y-position.

When the position of the square is calculated, the `selectSquare()` (for left-click) and `flagSquare()` (for right-click) methods are utilized. The former method makes sure the square is not null, its state is hidden (it has not already been revealed)
and that the board is not locked, which is set with a boolean variable. The method then utilized the view method `pushSelectEvent()` to push the select event with the location of the selected square. The latter of the two methods also makes sure the clicked square is not null, checks that the squares state is not value (it has not already been revealed) and the board is not locked.
To then utilize the view `pushFlagEvent()` method to push a flag event with the location of the selected square to be flagged.

#### 6.5.3. Menu
To create the menu of the game with the text to view the current "game state" and interactive buttons to restart the game and quit the game. A `JLabel` is created in the `initializeMenuText()` method
with a set text "Minesweeper" which is to represent that the game is in a playing state.

The restart `JButton` is initialized in the `initializeRestartButton()` method where the
"Restart" text is added to the button. The method calls the `addRestartButtonListener()` method which adds a action listener to the button for calling the view `pushResetGameEvent()` method
for pushing a reset event. The `initializeQuitResetButtons()` method is also called in the initialization of the button to set a size, add a background color/border, make sure the button is enabled and that the button
is initially hidden as these menu buttons should only appear when the game is either over or won. The quit `JButton` is initialized in the `initializeQuitButton()` method were the "Quit" text is added, calls the
`addQuitButtonListener()` method to add an action listener to exit the program with the exit system call. Like the reset button its appearance and making the button enabled/hidden is made
in the common `initializeQuitResetButtons()` method.

To easier place the menu text and buttons in the GUI, a `JPanel` is initialized to hold the three components in the `initializeMenu()` method. The `JPanel` is initialized with a background color/border, a preferred size
of the game width and 10% of the games height as the menu is only to hold a small portion of the GUI and a grid layout to place the three components next to each-other.

#### 6.5.4. Game panel
To make the placement of the menu and board in the frame easier, a `JPanel` was initialized to hold the two components in the `initializeGamePanel()` method. The method initializes the
`JPanel` with a border layout to place the menu at the top of the GUI and the board in the center. To make sure the menu and board is correctly scaled if the width and height of the game
were ever to change, the `JPanel` sets a preferred size of the width of the game, and for the height the games height plus an extra 10% of the height. Only utilizing the games width
and height as preferred size gave undesirable gaps between the menu and board if the width and height was set to a smaller size.

#### 6.5.5. Frame
The `JFrame` of the game which holds the game panel containing the board and menu is initialized in the `initializeFrame()` method. The methods simply adds the game panel, makes sure
the game exits when the window is closed, makes sure the window cant be resized, sets the game title "Minesweeper" in the window, makes sure its opened in the center of the monitor and
that the window is visable.

#### 6.5.6. Setters
The view also provides the setter methods `setHidden()`, `setFlagged()`, `setValue()` and `setMine()` to call the previously mentioned square methods for state and visual change. The setter methods
themselves are defined in the `View` superclass, but are abstract and utilized by the `GameController`, except for the `setMine()` method which is utilized in the `gameOver()` method,
but that will be explained further down.

The set methods simply calls the matching squares methods utilizing the `getSquareFromPosition()` method to access a specified square in the board from the provided point location. The method simply makes sure the provided location is not null,
or invalid (outside the game board) to then return a square from the board by utilizing the board size and the x/y-point location.

#### 6.5.7. Game over, win, reset
The view also provides methods for displaying that the game is over `gameOver()`, the game is won `win()` and a method for resetting the game `reset()`.
These methods are also defined in the `View` superclass, but are abstract and utilized by the `GameController`.

The `gameOver()` method simply calls the `setMine()` method (which is why indirect the `setMine()` is only utilized in the `GameController`) with the provided point location
to display that a mine has been hit. The menu text is changed from "Minesweeper" to "Game over!", the board lock is set to make sure the square cant be clicked and
the menu buttons are set to visible.

The `win()` method changes the menu text from "Minesweeper" to "You win!", sets the board lock and makes the menu buttons visible.

And finally the `reset()` method loops through the board and sets every square back to their initial hidden state, sets the menu title back to "Minesweeper", unlocks the board lock and
hides the menu buttons once again.

### 6.6. Controller implementation
As defined by the MVC pattern, the controller is responsible for directing communications between the model and view. To make the communication easier, the observer pattern was utilized
to make the controller observe the observable model and view to then utilize push event methods.

The `Model` and `View` is initialized from provided parameter in the constructor. As the two components are observable and extends the abstract `observed` class, the `attachObserver()` method is utilized to attach
the controller as an Observer for both the components.

#### 6.6.1. Handle events
The controller provides the methods `handleSelectSquareEvent()`, `handleFlagSquareEvent()`, `handleResetGameEvent()`, `handleRevealSquareEvent()`, `handleMineHitEvent()` and `handleWinEvent()` for handling events.
These methods are also defined in the `Controller` superclass, but are abstract and utilized in the `Controller` `handleEvent()` method.

The `handleSelectSquareEvent()` method which is called when the user left-clicks on a square (via `handleEvent()`) simply calls the `Model` `selectSquare()` method with the provided point location of the square in the board.

The `handleFlagSquareEvent()` method which is called when the user right-clicks on a square (via `handleEvent()`) calls the `Model` `setSquareFlag()` method with the provided point location and the `Model` `isFlagged()` to make sure already flagged squares in the model is not flagged any more
and non flagged squares gets flagged. When the square in the model is set (flagged or not flagged anymore) the `View` `setFlagged()` or `setHidden()` method is called depending if the square in the model
is now flagged, in which the `setFlagged()` is called or not flagged anymore, in which the `setHidden()` is called to remove the flag and reset the square to hidden.

The `handleResetGameEvent()` method which is called when the user clicks on the restart menu button (via `handleEvent()`) calls the respective `Model` and `View` `reset()` methods for resetting the game.

The `handleRevealSquareEvent()` method which is called when the clicked square in `Model` is not a mine, not already revealed or flagged (via `handleEvent()`) calls the `View` `setValue()` method
with each point location provided from the reveal event and the value to be set from the `Model` `getSquareValue()`.

The `handleMineHitEvent()` method which is called when the clicked square in `Model` is a mine (via `handleEvent()`) calls the `View` `gameOver()` method with the provided point location
of the mine.

The `handleWinEvent()` method which is called when the squares left in the game is only mines in `Model` (via `handleEvent()`) calls the `View` `win()` method.

## 7. Discussion <a name="section-7"></a>

### 7.1. Fulfillment of purpose

The success of the project is determined by its fulfillment of the purpose statement defined. The simplest way to determine this is to go through the purpose point by point and evaluating each section for compliance:

* __"The goal of this project is to create an application which is a capable implementation of the classic Microsoft game Minesweeper. ..."__

This requirement is quite basic and defines the bare minimum needed from the implementation; "the game must be playable". The wording is quite vague, however, the game is very much playable and does provide all required functions. So therefore this requirement can be considered fulfilled.

* __"... The application should implement a Swing GUI as the medium for user interaction with the game. ..."__

The requirement states that the Java Swing framework must be used to provide the user interface for the game. This is fulfilled as the main GUI handler, the `GameView` is implemented using Swing. The `GameView` makes use of a JFrame to create a window, and then builds components for each part of the game.

* __"... The GUI should allow for all of the standard Minesweeper actions; viewing the board, clicking squares to reveal them, and flagging squares by right-clicking them. ..."__

This requirement is related to the two previous, in that it defines functions which the game implementation must provide. When the game starts the GUI is shown, which displays the board with all its squares. Then the user is able to click on squares to reveal them; and right-click to flag them. So therefore this requirement is fulfilled.

* __"... The game should be lost if a mine is clicked, and if all non-mine squares are revealed the game is won; and the user can either exit the game or restart. ..."__

This requirements specifies another set of requirements upon the correct implementation of the Minesweeper game logic. The GUI provides functionality for viewing and interacting with the board. And when squares are clicked, the backend (model) checks for clicked mines, which causes a lose event. And if all non-mine squares are revealed (without clicking any mines), a win event is triggered; which fulfills this requirement.

* __"... The general structure of the project implementation should also follow the MVC architectural pattern. With the game functionality being separated into data handling (model), user interaction (view), a communications layer (controller); and the Observer pattern being used to trigger events between the elements. ..."__

The application does make use of the observer pattern. The `Observed` and `Observer` class/interface outline the methods for interaction. Where observers attach themselves to observed objects; and observed objects can push events to all attached observers.

The application also follows the MVC architectural pattern through the implementation of the `Model`, `View` and `Controller` superclasses. These classes define all methods which are used when these types interact with each other. And there is a strict separation between the model and view. All communications go through the controller. Where the model and view can trigger events (using the observer classes) and the controller handles them and trigger appropriate methods upon the model and view.

All in all this means that this requirement has been met.

* __"... Lastly the project should also follow the principles of TDD with thorough unit-testing being implemented before each part of the functionality."__

During the implementation process the TDD principles were followed. All work was loosely planned before starting the project, and then the implementation was split into parts. Each section constituted a feature which was specifically documented in a Trello card. Each card defined the requirements for the feature; and the minimum of which tests needed to be created.

Then when implementing features, the requirements were again split into smaller parts. Each part constituted a testable part of the functionality to be added. Then before the implementation of each part, the test for the specific sub-requirement was created using the JUnit5 testing framework. And after implementing each requirement the implementations were iteratively refactored after passing the test, to uphold code quality standards.

This workflow follows what was defined before the project started, and does follow the principles of Test Driven Development as required; so the requirement is fulfilled.

### 7.2. Viability of implementation
When it comes to the quality/viability of the implementation there are a few considerations to be made; outside of just the requirement fulfillment. As determined in the previous section, the implementation does fulfill the requirements places on the project. However, there are a couple of thing which may warrant consideration in regards to what could be done differently.

First of all, the game does work; which means that there is nothing inherently broken about the methodology. But when it comes to the choice of platform for developing a game, Java may not have been the best choice. The Minesweeper game is not a resource hungry game, or at least if it is it is due to a bad implementation rather than a complex concept. But there is a reason why games are generally not developed in Java; and part of that is performance. Java provides a lot of functionality which makes it a great language for many scenarios. However, when it comes to games the performance cost (such as garbage collection) of this may not be worth it. For a small game like Minesweeper it does not really matter, but considering future development into more advanced games switching languages may be appropriate.

Another reason for switching away from Java, specifically because Minesweeper is a game, would be the requirement to have Java itself installed. Having to install additional separate software is a hassle if you are just an average user. And while Java is very common, it is not default software on most PCs. A good alternative would be to redevelop the game into a web application, which would make it accessible on basically any, since browsers are default software. And it is relatively easy to create wrapper software to make the game available offline, if that is of interest.

Lastly, there is a lot of improvement to be made with the "square generation" functionality provided by the `GameModel`. There are many existing ways to generate the board for the Minesweeper game. This simplest way, and the one used currently, is to randomize which squares are mines and provide a "grace area" around the first square to prevent instant loss. This works well, and if the ratio between board size and mine percentage is managed well, it can even be close to perfect.

But in theory (and rarely in practice) it is possible for an impossible board to be generated. Mines could be placed in such a way that you have to guess where the mine is, and therefore the game is no longer based on skill, and instead luck. There are multiple algorithms to achieve "always possible" boards which could be implemented. One such way is to randomize the board, but then use a set of rules to validate that the board is still solvable; and if it isn't modify the mine placements. Since the game is not particularly complicated, the validation isn't overly complicated; but it is still a lot more complicated than a random "grace area" method.

### 7.3. Personal reflections
Working with TDD was definitely challenging, it required good planning and a very methodical approach. There was a lot to be learned, everything from trying to structure simple but effective tests, to making the small simple implementations of them. It is definitely a learning-curve, as the approach of implementing the logic before the tests, or no tests at all had previously been the working approach of previous courses.

Another very important part of the project that almost every human can benefit learning from is working in a team. This time we were only two people working on the project, but it still required good planning and communication.

The hardest part was definitely making the tests. Finding how to test something, how to know if the logic runs correctly and what else can go wrong in the logic to be implemented or somewhere else in the code was very difficult. A good example is the need of testing abstract classes or interfaces, where there were instances where non-instantiable elements provided default implementations of methods. This was a little tricky, and only testing their concrete implementations could result in a lost of test coverage in the default implementations.

However, the learning modules did prepare us sufficiently for the challenge. They were well-structured with both concepts and code examples, which is a good combination to better grasp and understand the subjects.