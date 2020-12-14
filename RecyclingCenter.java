package Room_related;

public class RecyclingCenter extends Room {
    private Object[][] coordinateSystem;
    private Container metalContainer = new Container("Metal Container", TrashType.METAL);
    private Container hazardousContainer = new Container("Hazardous Container", TrashType.HAZARDOUSWASTE);
    private Container residualContainer = new Container("Residual Container", TrashType.RESIDUALWASTE);
    private Container plasticContainer = new Container("Plastic Container", TrashType.PLASTIC);
    private Container[] containers = {metalContainer, hazardousContainer, residualContainer, plasticContainer};
    public RecyclingCenter(String description, Object[][] coordinateSystem) {
        super(description, coordinateSystem);
    }

    @Override
    public Object[][] getCoordinateSystem() {
        return coordinateSystem;
    }
    @Override
    public void setCoordinateSystem(Object[][] coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }
}
