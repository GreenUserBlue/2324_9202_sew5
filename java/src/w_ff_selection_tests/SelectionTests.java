package w_ff_selection_tests;

import com.fourinachamber.fortyfive.keyInput.selection.FocusableParent;
import com.fourinachamber.fortyfive.keyInput.selection.SelectionTransition;
import com.fourinachamber.fortyfive.keyInput.selection.SelectionTransitionCondition;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SelectionTests {

    public static OnjScreen onjScreen = new OnjScreen();

    public static final Vector2 screenSize = new Vector2(975,975 * 9 / 16);

    public static void main(String[] args) throws InterruptedException, IOException {
        final String title = "Test Window";

        //Creating the frame.
        JFrame frame = new JFrame(title);

        frame.setSize((int) screenSize.x + 25, (int) screenSize.y + 25);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        //Creating the canvas.
        Canvas canvas = new Canvas();

        canvas.setSize((int) screenSize.x + 25, (int) screenSize.y + 25);
        canvas.setBackground(Color.BLACK);
        canvas.setVisible(true);
        canvas.setFocusable(false);

        //Putting it all together.
        frame.add(canvas);

        canvas.createBufferStrategy(3);

        BufferStrategy bufferStrategy;
        Graphics graphics;

        addActors();
        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));


        long counter = 0;
        while (true) {
            counter++;
            bufferStrategy = canvas.getBufferStrategy();
            graphics = bufferStrategy.getDrawGraphics();
            graphics.clearRect(0, 0, (int) screenSize.x, (int) screenSize.y);
//            if ((counter & 15) == 1) {
//                onjScreen.focusNext(null);
//                System.out.println(onjScreen.getFocusedActor());
//            }else if ((counter & 31) == 1){
//                onjScreen.focusPrevious();
//                System.out.println("THIS SHOULD NEVER HAPPEN");
//            }
            synchronized (onjScreen.getActors()) {
                for (Actor it : onjScreen.getActors()) {
                    if (it instanceof FocusableActor && ((FocusableActor) it).isFocusable()) {
                        if (((FocusableActor) it).isFocused()) {
                            graphics.setColor(Color.BLUE);
                        } else if ( ((FocusableActor) it).getGroup().equals("MyMiddleGroup")) {
                            graphics.setColor(Color.YELLOW);
                        }else {
                            graphics.setColor(Color.GREEN);
                        }
                    } else {
                        graphics.setColor(Color.RED);
                    }
                    graphics.drawOval((int) it.x, (int) screenSize.y - ((int) (it.y - it.height)), (int) it.width, (int) it.height);
                }
            }
            bufferStrategy.show();
            graphics.dispose();
            Thread.sleep(100);
            if (counter > 1){
                String lowerCase = reader.readLine().toLowerCase();
                if (lowerCase.isBlank()) continue;
                char name = lowerCase.charAt(0);
                switch (name){
                    case 'a' -> onjScreen.focusNext(new Vector2(-1,0));
                    case 's' -> onjScreen.focusNext(new Vector2(0,-1));
                    case 'w' -> onjScreen.focusNext(new Vector2(0,1));
                    case 'd' -> onjScreen.focusNext(new Vector2(1,0));
                    default -> System.out.println("incorrect char: '"+name+"'");
                }
            }
        }
    }

    private static void addActors() {
        Random rnd = new Random(4);
        ArrayList<String> groups = new ArrayList<String>();
        groups.add("MyBiggestGroup");
        groups.add("MyMiddleGroup");
        groups.add("MyLittleGroup1");
        groups.add("MyLittleGroup2");
        for (int i = 0; i < 5; i++) {
            MyActor temp = new MyActor(groups.get(rnd.nextInt(groups.size())), rnd.nextInt(975), rnd.nextInt(975*9/16));
            temp.setFocusable(rnd.nextDouble() > 0.2);
            onjScreen.getActors().add(temp);
        }

        onjScreen.getActors().add(new MyActor("MyMiddleGroup", 200.0, 200.0));
        onjScreen.getActors().add(new MyActor("MyMiddleGroup", 205.0, 249.0));
        onjScreen.getActors().add(new MyActor("MyMiddleGroup", 194.0, 300.0));

        FocusableParent myMiddleGroup = new FocusableParent(
                new ArrayList<>(Collections.singleton(new SelectionTransition(
                        SelectionTransition.TransitionType.SEAMLESS, new SelectionTransitionCondition.Always(),
                        new ArrayList<>(Collections.singleton("MyMiddleGroup"))
                ))), () -> null,
                "MyMiddleGroup",
                new ArrayList<>(),
                (a) -> null
        );
        onjScreen.getSelectionHierarchy().add(
                myMiddleGroup
        );
        myMiddleGroup.updateFocusableActors(onjScreen);
    }
}