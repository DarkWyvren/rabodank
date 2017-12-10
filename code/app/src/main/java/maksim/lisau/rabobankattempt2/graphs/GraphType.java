package maksim.lisau.rabobankattempt2.graphs;

/**
 * Created by Maks on 07-Oct-17.
 */
public enum GraphType{
    BAR,PIE,R_AREA /* ratio area chart  */,    ///// // non comparative (only one GraphStream needed)
    LINE,BOX_P /* box plot  */,                ///// // potientially comparative (> or = one GraphStream needed)
    COSMOS,NR_AREA /* non-ratio area chart  */;///// // comparative (> one GraphStream needed)
}
