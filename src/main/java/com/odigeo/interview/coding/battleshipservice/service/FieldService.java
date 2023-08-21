package com.odigeo.interview.coding.battleshipservice.service;

import com.odigeo.interview.coding.battleshipservice.model.Cell;
import com.odigeo.interview.coding.battleshipservice.model.Coordinate;
import com.odigeo.interview.coding.battleshipservice.model.ship.Ship;
import com.odigeo.interview.coding.battleshipservice.util.GameConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class FieldService {

    @Inject
    private CoordinateService coordinateService;

    public boolean allShipsSunk(Cell[][] field) {
        Set<Ship> visitedShips = new HashSet<>();
        int shipsSunkCounter = 0;
        int rowNumbers = field.length;
        int columnNumbers = field[0].length;

        for (int row = 0; row < rowNumbers; row++) {
            for (int column = 0; column < columnNumbers; column++) {
                Ship ship = field[row][column].getShip();
                if (ship != null && !visitedShips.contains(ship)) {
                    boolean isShipSunk = isShipSunk(field, ship);
                    if (isShipSunk) {
                        shipsSunkCounter++;
                    }
                    visitedShips.add(ship);
                }
            }
        }

        return visitedShips.size() == shipsSunkCounter;
    }

    public boolean isShipSunk(Cell[][] field, Ship ship) {
        boolean shipSunk = true;
        for (Coordinate coordinate : ship.getCoordinates()) {
            shipSunk &= field[coordinate.getRow()][coordinate.getColumn()].isHit();
        }
        return shipSunk;
    }

    public Cell[][] buildField(List<Ship> shipsDeployment) {
        Cell[][] field = buildWater();
        deployShips(field, shipsDeployment);
        return field;
    }

    private Cell[][] buildWater() {
        Cell[][] field = new Cell[GameConfiguration.FIELD_HEIGHT][GameConfiguration.FIELD_WIDTH];
        for (int row = 0; row < GameConfiguration.FIELD_HEIGHT; row++) {
            for (int col = 0; col < GameConfiguration.FIELD_WIDTH; col++) {
                field[row][col] = new Cell();
            }
        }
        return field;
    }

    private void deployShips(Cell[][] field, List<Ship> ships) {
        ships.forEach(ship ->
            ship.getCoordinates().forEach(coordinate ->
                    field[coordinate.getRow()][coordinate.getColumn()] = new Cell(ship)
            )
        );
    }

}
