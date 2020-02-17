export interface IDireccion {
  id?: number;
  direccion?: string;
  ciudad?: string;
  pais?: string;
}

export class Direccion implements IDireccion {
  constructor(public id?: number, public direccion?: string, public ciudad?: string, public pais?: string) {}
}
