package it.polimi.ingsw;

import java.io.Serializable;

public interface Request extends Serializable {
    Response handle(RequestHandler handler);
}