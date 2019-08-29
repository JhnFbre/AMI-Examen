export class Employee {

    constructor(_id = '', name = '', lastname = '', username = '', password = '') {
        this._id = _id;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }

    _id: string;
    name: string;
    lastname: string;
    username: string;
    password: string;
}