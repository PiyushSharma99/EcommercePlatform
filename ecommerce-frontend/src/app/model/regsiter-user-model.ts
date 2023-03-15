export class User {
    public name: string = '';
    public email: string = '';
    public password: string = '';
    public username: string = '';
    public role!: [];
    public userImage!: FormData;
    constructor(name:string,email:string,password:string,username:string, userImage:FormData) { 
        this.name = name 
        this.email = email
        this.password = password
        this.username = username
        this.userImage = userImage
     }  
     setArray(role : any):void { 
        this.role = role;
     } 
}