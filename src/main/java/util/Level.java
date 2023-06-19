package util;

import main.Application;
import obj.*;

import static util.Const.*;
import static util.Const.GUI.*;

import obj.collectible.AmmoBox;
import obj.collectible.Doc;
import obj.collectible.Finish;
import obj.collectible.HealthBox;
import obj.soldier.Enemy;
import obj.soldier.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Random;

/**
 * Level object of the game.
 * Handles information about all objects in this playing session.
 */
public class Level implements Serializable {
    private final Application app; // associated game application
    private BufferedImage background; // background image
    private Player player; // player object
    private Finish finish; // finish position object
    private int enemiesCount; // number of alive enemies
    private int docsCount; // number of documents left to collect
    private final LinkedList<Enemy> enemies = new LinkedList<>(); // list of enemy objects
    private final LinkedList<Doc> docs = new LinkedList<>(); // list of document objects
    private final LinkedList<HealthBox> healthBoxes = new LinkedList<>(); // list of health box objects
    private final LinkedList<AmmoBox> ammoBoxes = new LinkedList<>(); // list of ammo box objects
    private final LinkedList<Obstacle> obstacles = new LinkedList<>(); // list of obstacle objects
    private final LinkedList<Bullet> bullets = new LinkedList<>(); // list of bullet objects

    /**
     * Constructor for the Level.
     * Used in new level creation.
     *
     * @param app  associated Game object.
     */
    public Level(Application app) {
        loadBackground();
        this.app = app;
        player = new Player(0, 14*SPRITE, this);
        finish = new Finish(19*SPRITE, 3*SPRITE, this);
        enemiesCount = 0;
        docsCount = 0;
    }

    /**
     * Constructor for the Level.
     * Used in level loading.
     *
     * @param name  level filename.
     * @param dir   directory, where level file is located.
     * @param app   associated Game object.
     */
    public Level(String name, String dir, Application app) {
        loadBackground();
        loadLevel(name, dir);
        this.app = app;
        for (Enemy enemy : enemies) if (!enemy.isDead()) enemiesCount++; // Load alive enemies count
        for (Doc doc : docs) if (doc.isActive()) docsCount++; // Load documents left to collect count
    }

    /**
     * Loads information from binary JSON file.
     * Creates all game objects based on this information.
     *
     * @param name  level filename.
     * @param dir   directory, where level file is located.
     */
    public void loadLevel(String name, String dir) {
        String file = null;
        try {
            file = Files.readString(Paths.get(dir + "/" + name + ".bin"));
        } catch (IOException e) {
            app.getLogger().error("Unable to open level file. File wasn't found!"); // Logging
        }
        assert file != null;
        JSONObject input = new JSONObject(new JSONTokener(file));

        // Load all level objects
        player = new Player(input.getJSONObject(JSON.PLAYER), this);
        finish = new Finish(input.getJSONObject(JSON.FINISH), this);
        for (Object enemy : input.getJSONArray(JSON.ENEMY)) enemies.add(new Enemy((JSONObject) enemy, this));
        for (Object doc : input.getJSONArray(JSON.DOC)) docs.add(new Doc((JSONObject) doc, this));
        for (Object healthBox : input.getJSONArray(JSON.HEALTHBOX)) healthBoxes.add(new HealthBox((JSONObject) healthBox, this));
        for (Object ammoBox : input.getJSONArray(JSON.AMMOBOX)) ammoBoxes.add(new AmmoBox((JSONObject) ammoBox, this));
        for (Object obstacle : input.getJSONArray(JSON.OBSTACLE)) obstacles.add(new Obstacle((JSONObject) obstacle, this));
        for (Object bullet : input.getJSONArray(JSON.BULLET)) bullets.add(new Bullet((JSONObject) bullet, this));
    }

    /**
     * Saves all level information to binary JSON file.
     * Writes important game objects attributes.
     *
     * @param name  level filename.
     * @param dir   directory, where level file will be located.
     */
    public void saveLevel(String name, String dir) {
        JSONObject jsonLevel = new JSONObject();

        // Save all level objects
        try {
            jsonLevel.put(JSON.PLAYER, player.getJSON(GameObject.PLAYER));

            jsonLevel.put(JSON.FINISH, finish.getJSON());

            JSONArray jsonEnemies = new JSONArray();
            for (Enemy enemy : enemies) jsonEnemies.put(enemy.getJSON(GameObject.ENEMY));
            jsonLevel.put(JSON.ENEMY, jsonEnemies);

            JSONArray jsonDocs = new JSONArray();
            for (Doc doc : docs) jsonDocs.put(doc.getJSON());
            jsonLevel.put(JSON.DOC, jsonDocs);

            JSONArray jsonHealthBoxes = new JSONArray();
            for (HealthBox healthBox : healthBoxes) jsonHealthBoxes.put(healthBox.getJSON());
            jsonLevel.put(JSON.HEALTHBOX, jsonHealthBoxes);

            JSONArray jsonAmmoBoxes = new JSONArray();
            for (AmmoBox ammoBox : ammoBoxes) jsonAmmoBoxes.put(ammoBox.getJSON());
            jsonLevel.put(JSON.AMMOBOX, jsonAmmoBoxes);

            JSONArray jsonObstacles = new JSONArray();
            for (Obstacle obstacle : obstacles) jsonObstacles.put(obstacle.getJSON());
            jsonLevel.put(JSON.OBSTACLE, jsonObstacles);

            JSONArray jsonBullets = new JSONArray();
            for (Bullet bullet : bullets) jsonBullets.put(bullet.getJSON());
            jsonLevel.put(JSON.BULLET, jsonBullets);
        } catch (JSONException e) {
            app.getLogger().error("Unable to proceed JSON object!"); // Logging
        }

        try {
            FileOutputStream file = new FileOutputStream(dir + "/" + name + ".bin");
            file.write(jsonLevel.toString().getBytes(StandardCharsets.UTF_8));
            file.close();
        } catch (IOException e) {
            app.getLogger().error("Unable to save level!"); // Logging
        }
    }

    /**
     * Updates the level state.
     * Forwards the event to the appropriate objects.
     */
    public void update() {
        player.update();
        for (Enemy enemy: enemies) enemy.update();
        for (Doc doc: docs) doc.update();
        for (HealthBox healthBox: healthBoxes) healthBox.update();
        for (AmmoBox ammoBox: ammoBoxes) ammoBox.update();
        try {
            for (Bullet bullet: bullets) bullet.update();
        } catch (ConcurrentModificationException e) {
            // Avoiding error when new bullet was created with active update
        }
    }

    /**
     * Renders the level on the screen.
     * Forwards the event to the appropriate objects.
     *
     * @param graphics Graphics object used to draw level objects.
     */
    public void render(Graphics graphics) {
        graphics.drawImage(background, 0, 0, null);

        finish.render(graphics);
        for (Enemy enemy: enemies) if (enemy.isDead()) enemy.render(graphics); // Rendering dead enemies
        if (player.isDead()) player.render(graphics); // Rendering dead player
        for (Doc doc: docs) doc.render(graphics);
        for (HealthBox healthBox: healthBoxes) healthBox.render(graphics);
        for (AmmoBox ammoBox: ammoBoxes) ammoBox.render(graphics);
        for (Obstacle obstacle: obstacles) obstacle.render(graphics);
        for (Enemy enemy: enemies) if (!enemy.isDead()) enemy.render(graphics); // Rendering alive enemies
        if (!player.isDead()) player.render(graphics); // Rendering alive player

        try {
            for (Bullet bullet: bullets) bullet.render(graphics);
        } catch (ConcurrentModificationException e) {
            // Avoiding error when new bullet was created with active update
        }
    }

    /**
     * Randomly loads background for the level.
     */
    private void loadBackground() {
        String[] tmp = {GRASS, SAND, MUG, SNOW};
        int rnd = new Random().nextInt(tmp.length); // Randomly choose from 4 backgrounds
        background = Image.loadImage(tmp[rnd]);
    }

    /**
     * Returns the associated game object.
     *
     * @return associated game object.
     */
    public Application getApp() {return app;}

    /**
     * Returns player of the level.
     *
     * @return Player object.
     */
    public Player getPlayer() {return player;}

    /**
     * Sets actual player object.
     *
     * @param player actual player object.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Returns finish position of the level.
     *
     * @return Finish object.
     */
    public Finish getFinish() {return finish;}

    /**
     * Returns list of obstacles of the level.
     *
     * @return Obstacle objects list.
     */
    public LinkedList<Obstacle> getObstacles() {return obstacles;}

    /**
     * Returns list of health boxes of the level.
     *
     * @return HealthBox objects list.
     */
    public LinkedList<HealthBox> getHealthBoxes() {
        return healthBoxes;
    }

    /**
     * Returns list of ammo boxes of the level.
     *
     * @return AmmoBox objects list.
     */
    public LinkedList<AmmoBox> getAmmoBoxes() {
        return ammoBoxes;
    }

    /**
     * Returns list of enemies of the level.
     *
     * @return Enemy objects list.
     */
    public LinkedList<Enemy> getEnemies() {return enemies;}

    /**
     * Returns list of documents of the level.
     *
     * @return Doc objects list.
     */
    public LinkedList<Doc> getDocs() {return docs;}

    /**
     * Returns actual alive enemies count.
     *
     * @return alive enemies count.
     */
    public int getEnemiesCount() {return enemiesCount;}

    /**
     * Returns actual documents count to collect.
     *
     * @return documents count to collect.
     */
    public int getDocsCount() {return docsCount;}

    /**
     * Sets actual alive enemies count as a reaction to new adding/deleting.
     *
     * @param enemiesCount alive enemies count.
     */
    public void setEnemiesCount(int enemiesCount) {this.enemiesCount = enemiesCount;}

    /**
     * Sets actual documents count to collect as a reaction to new adding/deleting.
     *
     * @param docsCount documents count to collect.
     */
    public void setDocsCount(int docsCount) {this.docsCount = docsCount;}

    /**
     * Makes new enemy at the level.
     * Respects maximum enemies amount.
     *
     * @param x x-coordinate of adding object.
     * @param y y-coordinate of adding object.
     */
    public void createEnemy(int x, int y) {
        if (enemies.size() != Limits.ENEMIES) {
            Enemy enemy = new Enemy(x, y, this);
            enemies.add(enemy);
            if (app != null) app.getLogger().editor("Enemy " + enemy + " was placed at [" + x + "," + y + "]. Enemies count: " + enemies.size() + "."); // Logging
        } else {
            if (app != null) app.getAudio().playSound(Sounds.ERROR);
        }
    }

    /**
     * Makes new document at the level.
     * Respects maximum documents amount.
     *
     * @param x x-coordinate of adding object.
     * @param y y-coordinate of adding object.
     */
    public void createDoc(int x, int y) {
        if (docs.size() != Limits.DOCS) {
            Doc doc = new Doc(x, y, this);
            docs.add(doc);
            if (app != null) app.getLogger().editor("Document " + doc + " was placed at [" + x + "," + y + "]. Documents count: " + docs.size() + "."); // Logging
        } else {
            if (app != null) app.getAudio().playSound(Sounds.ERROR);
        }
    }

    /**
     * Makes new health box at the level.
     * Respects maximum health boxes amount.
     *
     * @param x x-coordinate of adding object.
     * @param y y-coordinate of adding object.
     */
    public void createHealthBox(int x, int y) {
        if (healthBoxes.size() != Limits.HEALTHBOX) {
            HealthBox healthBox = new HealthBox(x, y, this);
            healthBoxes.add(healthBox);
            if (app != null) app.getLogger().editor("Health box " + healthBox + " was placed at [" + x + "," + y + "]. Health boxes count: " + healthBoxes.size() + "."); // Logging
        } else {
            if (app != null) app.getAudio().playSound(Sounds.ERROR);
        }
    }

    /**
     * Makes new ammo box at the level.
     * Respects maximum ammo boxes amount.
     *
     * @param x x-coordinate of adding object.
     * @param y y-coordinate of adding object.
     */
    public void createAmmoBox(int x, int y) {
        if (ammoBoxes.size() != Limits.AMMOBOX) {
            AmmoBox ammoBox = new AmmoBox(x, y, this);
            ammoBoxes.add(ammoBox);
            if (app != null) app.getLogger().editor("Ammo box " + ammoBox + " was placed at [" + x + "," + y + "]. Ammo boxes count: " + ammoBoxes.size() + "."); // Logging
        } else {
            if (app != null) app.getAudio().playSound(Sounds.ERROR);
        }
    }

    /**
     * Makes new obstacle at the level.
     *
     * @param x x-coordinate of adding object.
     * @param y y-coordinate of adding object.
     */
    public void createObstacle(int x, int y) {
        Obstacle obstacle = new Obstacle(x, y, this);
        obstacles.add(obstacle);
        if (app != null) app.getLogger().editor("Obstacle " + obstacle + " was placed at [" + x + "," + y + "]. Obstacles count: " + obstacles.size() + "."); // Logging
    }

    /**
     * Makes new bullet at the level.
     *
     * @param x         x-coordinate of adding object.
     * @param y         x-coordinate of adding object.
     * @param direction bullet movement direction.
     * @param level     associated Level object.
     */
    public void createBullet(int x, int y, int direction, Level level) {
        bullets.add(new Bullet(x, y, direction, level));
    }

    /**
     * Deletes existing game object from the level.
     *
     * @param x x-coordinate of deleting object.
     * @param y y-coordinate of deleting object.
     */
    public void deleteObject(int x, int y) {
        for (Enemy enemy : enemies) { // Control if there was enemy in provided cell
            if (enemy.getX() == x && enemy.getY() == y) {
                enemies.remove(enemy);
                enemiesCount = enemies.size();
                if (app != null) app.getLogger().editor("Enemy " + enemy + " was removed from [" + x + "," + y + "]. Enemies count: " + enemies.size() + "."); // Logging
                break;
            }
        }

        for (Doc doc : docs) { // Control if there was document in provided cell
            if (doc.getX() == x && doc.getY() == y) {
                docs.remove(doc);
                docsCount = docs.size();
                if (app != null) app.getLogger().editor("Document " + doc + " was removed from [" + x + "," + y + "]. Documents count: " + docs.size() + "."); // Logging
                break;
            }
        }

        for (HealthBox healthBox : healthBoxes) {  // Control if there was health box in provided cell
            if (healthBox.getX() == x && healthBox.getY() == y) {
                healthBoxes.remove(healthBox);
                if (app != null) app.getLogger().editor("Health box " + healthBox + " was removed from [" + x + "," + y + "]. Health boxes count: " + healthBoxes.size() + "."); // Logging
                break;
            }
        }

        for (AmmoBox ammoBox : ammoBoxes) {  // Control if there was ammo box in provided cell
            if (ammoBox.getX() == x && ammoBox.getY() == y) {
                ammoBoxes.remove(ammoBox);
                if (app != null) app.getLogger().editor("Ammo box " + ammoBox + " was removed from [" + x + "," + y + "]. Ammo boxes count: " + ammoBoxes.size() + "."); // Logging
                break;
            }
        }

        for (Obstacle obstacle : obstacles) {  // Control if there was obstacle in provided cell
            if (obstacle.getX() == x && obstacle.getY() == y) {
                obstacles.remove(obstacle);
                if (app != null) app.getLogger().editor("Obstacle " + obstacle + " was removed from [" + x + "," + y + "]. Obstacles count: " + obstacles.size() + "."); // Logging
                break;
            }
        }
    }
}
