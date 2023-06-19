package util;

import java.awt.*;

/**
 * Class with all the "magic" constants used in game.
 */
public class Const {
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 960;
    public static final int FPS = 120;
    public static final int UPS = 360;

    /**
     * Main limits in the game.
     */
    public static class Limits {
        public static final int LEVELS = 16;
        public static final int HEALTH_MIN = 1;
        public static final int HEALTH_MAX = 9;
        public static final int HEALTH_ENEMY = 3;
        public static final int AMMO_MIN = 0;
        public static final int AMMO_MAX = 99;
        public static final int ENEMIES = 49;
        public static final int DOCS = 49;
        public static final int HEALTHBOX = 49;
        public static final int HEALTHBOX_EFFECT = 3;
        public static final int AMMOBOX = 49;
        public static final int AMMOBOX_EFFECT = 30;
        public static final float SPEED = 0.4f;
        public static final float BULLET = 1.5f;
        public static final int REALOAD = 150;
        public static final int RANGE = 350;
    }

    /**
     * Soldiers constants (action and direction).
     */
    public static class Soldier {
        public static final int WALK = 0;
        public static final int SHOOT = 1;
        public static final int DIE = 2;
        public static final int UP = 0;
        public static final int RIGHT = 1;
        public static final int DOWN = 2;
        public static final int LEFT = 3;
    }

    /**
     * Game states constants.
     */
    public static class Stages {

        /**
         * Menu state constants.
         */
        public static class Menu {
            public static final int MAIN = 0;
            public static final int START = 1;
            public static final int PLAY = 2;
            public static final int EXIT = 3;
        }

        /**
         * Tutorial state constants.
         */
        public static class Tutorial {
            public static final int FIRST = 0;
            public static final int LAST = 4;
        }

        /**
         * Load state constants.
         */
        public static class Load {
            public static final int NEW = 0;
            public static final int LOAD = 1;
            public static final int CREATE = 2;
            public static final int UPDATE = 3;
        }

        /**
         * Editor state constants.
         */
        public static class Editor {
            public static final int NEW = -3;
            public static final int LOAD = -1;
            public static final int MAIN = 0;
            public static final int PAUSE = 1;
            public static final int EXIT = 2;
            public static final int OBJECTS = 3;
        }

        /**
         * Game state constants.
         */
        public static class Game {
            public static final int NEW = -2;
            public static final int LOAD = -1;
            public static final int PLAY = 0;
            public static final int PAUSE = 1;
            public static final int EXIT = 2;
            public static final int WIN = 4;
            public static final int LOSE = 5;
        }
    }

    /**
     * Constants to work with JSON files.
     */
    public static class JSON {
        public static final String PLAYER = "player";
        public static final String FINISH = "finish";
        public static final String ENEMY = "enemies";
        public static final String DOC = "docs";
        public static final String HEALTHBOX = "healthboxes";
        public static final String AMMOBOX = "ammoboxes";
        public static final String OBSTACLE = "obstacles";
        public static final String BULLET = "bullets";

        public static final String X = "x";
        public static final String Y = "y";
        public static final String DIRECTION = "direction";
        public static final String HEALTH = "health";
        public static final String AMMO = "ammo";
        public static final String LIMIT = "limit";
        public static final String ACTIVE = "active";
    }

    /**
     * Constants of the GUI elements.
     */
    public static class GUI {
        public static final String SOLDIERS = "/objs/soldiers.png";
        public static final String BULLET = "/objs/bullet.png";
        public static final String ITEMS = "/objs/items.png";
        public static final String BLOCKS = "/objs/blocks.png";
        public static final String BUTTONS_S = "/misc/buttons_S.png";
        public static final String BUTTONS_L = "/misc/buttons_L.png";
        public static final String BUTTONS_E = "/misc/buttons_E.png";
        public static final String DIGITS = "/misc/digits.png";
        public static final String GRASS = "/screen/grass.png";
        public static final String SAND = "/screen/sand.png";
        public static final String MUG = "/screen/mug.png";
        public static final String SNOW = "/screen/snow.png";
        public static final String MENU_1 = "/screen/menu.png";
        public static final String MENU_2 = "/screen/menu_2.png";
        public static final String PAUSE_1 = "/screen/pause_1.png";
        public static final String PAUSE_2 = "/screen/pause_2.png";
        public static final String PAUSE_3 = "/screen/pause_3.png";
        public static final String PAUSE_4 = "/screen/pause_4.png";
        public static final String PAUSE_5 = "/screen/pause_5.png";
        public static final String TUTOR = "/misc/tutor.png";
        public static final String FONT = "/misc/font.ttf";

        public static final int SPRITE = 64;
        public static final Font VETERAN = new Font("Veteran Typewriter", Font.PLAIN, 50);

        /**
         * Color constants.
         */
        public static class Colors {
            public static final Color KHAKI = new Color(220,200,140, 255);
            public static final Color OLIVE = new Color(53,55,23, 255);
            public static final Color KHAKI_A = new Color(220,200,140, 180);
            public static final Color OLIVE_A = new Color(53,55,23, 180);
            public static final Color ASPHALT = new Color(50,50,50);
            public static final Color TRANSPARENT = new Color(0,0,0, 0);
        }

        /**
         * Buttons constants.
         */
        public static class Buttons {
            public static final int INACTIVE = 0;
            public static final int ACTIVE = 1;
            public static final int PRESSED = 2;

            /**
             * Large buttons constants.
             */
            public static class Large {
                public static final int W = 500;
                public static final int H = 100;
                public static final int X = 390;
                public static final int Y_POS_1 = 400;
                public static final int Y_POS_2 = 550;
                public static final int Y_POS_3 = 700;

                public static final int PLAY = 0;
                public static final int TUTORIAL = 1;
                public static final int PLAYMISSION = 2;
                public static final int CREATEMISSION = 3;
                public static final int NEWGAME = 4;
                public static final int LOADGAME = 5;
                public static final int BACK = 6;
                public static final int QUIT = 7;
            }

            /**
             * Small buttons constants.
             */
            public static class Small {
                public static final int W = 300;
                public static final int H = 100;
                public static final int X = 950;
                public static final int Y_POS_1 = 30;
                public static final int Y_POS_2 = 180;
                public static final int Y_POS_3 = 330;

                public static final int START = 0;
                public static final int LOAD = 1;
                public static final int CREATE = 2;
                public static final int EDIT = 3;
                public static final int PREV = 4;
                public static final int NEXT = 5;
                public static final int YES = 6;
                public static final int NO = 7;
                public static final int RESUME = 8;
                public static final int AGAIN = 9;
                public static final int BACK = 10;
                public static final int QUIT = 11;
            }

            /**
             * Pause buttons constants.
             */
            public static class Pause {
                public static final int W = 300;
                public static final int H = 100;
                public static final int X_POS_1_1 = 320;
                public static final int X_POS_1_2 = 660;
                public static final int Y_POS_1 = 590;
                public static final int X_POS_2 = 480;
                public static final int Y_POS_2_1 = 440;
                public static final int Y_POS_2_2 = 590;
            }


            /**
             * Editor buttons constants.
             */
            public static class Edit {
                public static final int W = 100;
                public static final int H = 100;
                public static final int X_POS_1 = 450;
                public static final int X_POS_2 = 700;
                public static final int X_POS_3 = 320;
                public static final int X_POS_4 = 580;
                public static final int X_POS_5 = 830;
                public static final int Y_POS_1 = 320;
                public static final int Y_POS_2 = 450;
                public static final int Y_POS_3 = 580;

                public static final int PLAYER = 0;
                public static final int ENEMY = 1;
                public static final int DOC = 2;
                public static final int HEALTH = 3;
                public static final int AMMO = 4;
                public static final int OBSTACLE = 5;
                public static final int FINISH = 6;
            }

            /**
             * Radio buttons constants.
             */
            public static class Radio {
                public static final int X = 80;
                public static final int Y = 120;
                public static final int OFFSET = 50;
                public static final int W = 800;
                public static final int H = 50;
            }
        }

        /**
         * Digits constants.
         */
        public static class Digits {
            public static final int W = 30;
            public static final int H = 64;
            public static final int X_POS_1_1 = 450;
            public static final int X_POS_1_2 = 480;
            public static final int X_POS_2_1 = 1050;
            public static final int X_POS_2_2 = 1080;
            public static final int Y_POS_1 = 30;
            public static final int Y_POS_2 = 90;
        }

        /**
         * Text field constants.
         */
        public static class Text {
            public static final int W = 600;
            public static final int H = 50;
        }

        /**
         * Game objects constants.
         */
        public static class GameObject {
            public static final int W = 100;
            public static final int H = 100;

            public static final int PLAYER = 0;
            public static final int ENEMY = 1;
            public static final int DOC = 2;
            public static final int HEALTH = 3;
            public static final int AMMO = 4;
            public static final int OBSTACLE = 5;
            public static final int FINISH = 6;
            public static final int BULLET = 10;
        }
    }

    /**
     * Audio constants.
     */
    public static class Sounds {
        public static final int MENU = 0;
        public static final int GAME = 1;
        public static final int ERROR = 0;
        public static final int SHOOT = 1;
        public static final int EMPTY = 2;
        public static final int HIT = 3;
        public static final int DIE = 4;
        public static final int AMMO = 5;
        public static final int HEALTH = 6;
        public static final int DOC = 7;
        public static final int WIN = 8;
        public static final int DEFEAT = 9;

        public static final String[] SONGS = {"menu", "game"};
        public static final String[] SOUNDS = {"error", "shoot", "empty", "hit", "die", "ammo", "health", "doc", "win", "defeat"};

        public static final float VOLUME = 0.85f;
    }
}
