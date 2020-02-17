import { IFactura } from 'app/shared/model/factura.model';
import { IdentificacionTipo } from 'app/shared/model/enumerations/identificacion-tipo.model';

export interface ICliente {
  id?: number;
  descripcion?: string;
  identificacion?: string;
  identificacionTipo?: IdentificacionTipo;
  direccion?: string;
  facturas?: IFactura;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public descripcion?: string,
    public identificacion?: string,
    public identificacionTipo?: IdentificacionTipo,
    public direccion?: string,
    public facturas?: IFactura
  ) {}
}
