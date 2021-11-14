package es.ucm.gdv.blas.oses.carreau.lib;

/**
 * Enum con los diferentes tipos de pista que podemos conseguir en el juego
 */
public enum TipoPista {

    ValueReached,            //'This number can see all its dots ', // [1] No cerrada y visibles == DefVisibles
    WouldExceed,             //'Looking further in one direction would exceed this number' , [2]
    OneDirectionRequired,    //'One specific dot is included <br>in all solutions imaginable ', // [3] todas las casillasCambiables[dir] <= CasillasDef - CasillasVisibles

    ErrorClosedTooLate,      // 'This number sees a bit too much ', // [4]  y visibles > ValorDef
    ErrorClosedTooEarly,     // 'This number can\'t see enough', // [5]  visibles < ValorDef
    MustBeWall,              // 'This one should be easy... ', [6.1] //si es vacia

    LockedIn,                // 'A blue dot should always see at least one other',[6.2/7] //si es azul (isMod() == true && cerrada && EstadoCelda.Azul)

    ImposibleVision,         // 'Imposible to fill the vision of this tile ' < Def - visibles' [10]
}
