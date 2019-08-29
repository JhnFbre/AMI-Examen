export class Employee {

    constructor(_id = '', name = '', lastname = '', dni = '', phone = '', email = '', sale = '') {
        this._id = _id;
        this.customer = name;
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.phone = phone;
        this.email = email;
        this.sale = sale;
    }

    _id: string;
    customer: string;
    name: string;
    lastname: string;
    dni: string;
    phone: string;
    email: string;
    sale: string;
}