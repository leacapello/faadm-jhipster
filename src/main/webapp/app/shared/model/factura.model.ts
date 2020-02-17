import { Moment } from 'moment';
import { ICliente } from 'app/shared/model/cliente.model';

export interface IFactura {
  id?: number;
  identificacion?: string;
  fecha?: Moment;
  monto?: number;
  fechaVencimiento?: Moment;
  facturadRealizada?: boolean;
  facturaEnviada?: boolean;
  facturaCobrada?: boolean;
  clientes?: ICliente[];
}

export class Factura implements IFactura {
  constructor(
    public id?: number,
    public identificacion?: string,
    public fecha?: Moment,
    public monto?: number,
    public fechaVencimiento?: Moment,
    public facturadRealizada?: boolean,
    public facturaEnviada?: boolean,
    public facturaCobrada?: boolean,
    public clientes?: ICliente[]
  ) {
    this.facturadRealizada = this.facturadRealizada || false;
    this.facturaEnviada = this.facturaEnviada || false;
    this.facturaCobrada = this.facturaCobrada || false;
  }
}
