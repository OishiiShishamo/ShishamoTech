package shishamo_tech.common.machine.electric;

import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import java.util.List;
import java.util.Map;

/**
 * A {@link MultiblockShapeInfo.ShapeInfoBuilder} that keeps its aisles and
 * symbol map accessible, so coil-tier previews can be generated programmatically
 * by replacing the coil symbol with each tier's coil block.
 */
public class STAccessibleShapeInfoBuilder extends MultiblockShapeInfo.ShapeInfoBuilder {

    public List<String[]> getAisles() {
        return shape;
    }

    public Map<Character, BlockInfo> getSymbolMap() {
        return symbolMap;
    }
}
