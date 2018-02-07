package net.gegy1000.slyther.game;

import net.gegy1000.slyther.game.entity.*;
import net.gegy1000.slyther.network.NetworkManager;
import net.gegy1000.slyther.util.BridedList;
import net.gegy1000.slyther.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class Game<NET extends NetworkManager, CFG extends Configuration> {
    private BridedList<Entity<?>> entities = new BridedList<>();
    private BridedList<Snake<?>> snakes = new BridedList<>();
    private BridedList<Sector<?>> sectors = new BridedList<>();
    private BridedList<Food<?>> foods = new BridedList<>();
    private BridedList<Prey<?>> preys = new BridedList<>();

    public List<LeaderboardEntry> leaderboard = new ArrayList<>();
    public boolean[][] map = new boolean[80][80];

    public NET networkManager;

    public CFG configuration;

    private Queue<FutureTask<?>> tasks = new LinkedBlockingDeque<>();

    public void scheduleTask(Callable<?> callable) {
        tasks.add(new FutureTask<>(callable));
    }

    protected final void runTasks() {
        while (tasks.size() > 0) {
            FutureTask<?> task = tasks.poll();
            try {
                task.run();
                task.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addSector(Sector<?> sector) {
        Log.info("sector.x:{}, sector.y:{}", sector.posX, sector.posY);
        if (!sectors.contains(sector)) {
            sectors.add(sector);
        }
    }

    public void removeSector(Sector<?> sector) {
        Log.info("sector.x:{}, sector.y:{}", sector.posX, sector.posY);
        sectors.remove(sector);
    }

    public Sector<?> getSector(int x, int y) {
        for (Sector<?> sector : sectors) {
            if (sector.posX == x && sector.posY == y) {
                return sector;
            }
        }
        return null;
    }

    public List<Entity> getMovingEntitiesInSector(Sector sector) {
        List<Entity> entities = new ArrayList<>();
        for (Entity entity : this.entities) {
            if (entity.canMove() && entity.shouldTrack(sector)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public void addEntity(Entity<?> entity) {
        if (!entities.contains(entity)) {
            entities.add(entity);
            if (entity instanceof Snake) {
                snakes.add((Snake) entity);
            } else if (entity instanceof Food) {
                foods.add((Food) entity);
            } else if (entity instanceof Prey) {
                preys.add((Prey) entity);
            }
        }

        Log.info("new entity at:({},{}), type:{}, entities.len:{}, snakes.len:{}, foods.len:{}, preys.len:{}",
                entity.posX, entity.posY, entity.getClass().getSimpleName(), entities.size(), snakes.size(), foods.size(), preys.size());
    }

    public void removeEntity(Entity<?> entity) {
        if (entities.remove(entity)) {
            if (entity instanceof Snake) {
                snakes.remove(entity);
            } else if (entity instanceof Food) {
                foods.remove(entity);
            } else if (entity instanceof Prey) {
                preys.remove(entity);
            }
        }
    }

    public void clearEntities() {
        entities.clear();
        snakes.clear();
        foods.clear();
        preys.clear();
        sectors.clear();
    }

    public Iterator<Entity> entityIterator() {
        return new Iterator<Entity>() {
            private int index;

            private Entity lastEntity;

            @Override
            public boolean hasNext() {
                return index < entities.size();
            }

            @Override
            public Entity next() {
                return lastEntity = entities.get(index++);
            }

            @Override
            public void remove() {
                if (lastEntity == null) {
                    throw new IllegalStateException();
                }
                removeEntity(lastEntity);
                index--;
            }
        };
    }

    public List<Entity<?>> getEntities() {
        return entities.unmodifiable();
    }

    public List<Snake<?>> getSnakes() {
        return snakes.unmodifiable();
    }

    public List<Food<?>> getFoods() {
        return foods.unmodifiable();
    }

    public List<Sector<?>> getSectors() {
        return sectors.unmodifiable();
    }

    public List<Prey<?>> getPreys() {
        return preys.unmodifiable();
    }

    /** 游戏半径 */
    public abstract int getGameRadius();
    /** 蛇身最大长度，maximum snake length in body parts units*/
    public abstract int getMSCPS();
    public abstract int getSectorSize();
    public abstract int getSectorsAlongEdge();
    /** 根据蛇速计算角速度，coef. to calculate angular speed change depending snake speed */
    public abstract float getSpangDv();
    /** 貌似係节点速度，Maybe nsp stands for "node speed"?*/
    public abstract float getNsp1();
    public abstract float getNsp2();
    public abstract float getNsp3();

    /** 初始角速度，转弯用 basic snake angular speed*/
    public abstract float getBaseSnakeTurnSpeed();

    public abstract float getBasePreyTurnSpeed();
    public abstract float getCST();

    public abstract float getFPSL(int sct);
    public abstract float getFMLT(int sct);
}
