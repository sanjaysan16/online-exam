import React, { useEffect } from 'react'
import useStateRef from 'react-usestateref';


const LoginModal = () => {

  const [hasErrorRef, setHasError,refHasError] = useStateRef(false);
  
  

  const validateLoginForm = (key, value) => {
    
    switch (key) {
      case "username":
        var validation="[a-z0-9]+[@]{1}[a-z]{5}[.]{1}[com]";
        var regex = RegExp(validation);
        
        if(value === ""){
          console.log("i am empty username")
          document.getElementById("error_username").classList.remove('d-none');
          document.getElementById("error_username").classList.add('d-block');
          document.getElementById("error_username").innerHTML="*please enter a username"
          setHasError(true)
         }
        else{
          console.log(!regex.test(value),"i am regex")
          if(!regex.test(value)){
            
            setHasError(true)
            document.getElementById("error_username").classList.remove('d-none');
            document.getElementById("error_username").classList.add('d-block');
            document.getElementById("error_username").innerHTML="*please enter valid username"
          }
          
        }
        break;
      case "password":
        
        if(value===""){
          document.getElementById("error_password").classList.remove('d-none');
          document.getElementById("error_password").classList.add('d-block');
          document.getElementById("error_password").innerHTML="*please enter a password"
          setHasError(true)
        }
        else{
            document.getElementById("error_password").classList.add('d-none')
        }
        
        
    }
  }

  const handler = (e) => {
    e.preventDefault();
    setHasError(false)
    const data = new FormData(e.target);
    let username = data.get("username");
    console.log(username);
    let password = data.get("password");
    console.log(password);
    const value = Object.fromEntries(data.entries());
    document.getElementById("error_username").classList.add('d-none')
    document.getElementById("error_password").classList.add('d-none')
    Object.entries(value).map(([key, value]) => {
      validateLoginForm(key, value)
    })

    if (!refHasError.current) {
      fetch("https://localhost:8443/onlineexamapplication/control/checkLogin", {
        method: 'POST',
        credentials: 'include',
        body: JSON.stringify({ username: username, password: password }),
        headers: {
          'Content-Type': "application/json",
          'Accept': "application/json"
        },
      }

      )
        .then(response => response.json())
        .then(values => { console.log(values);
           const backendResult=values.isAdmin ;
           const lastName=values._lastName_;

           if(backendResult ===  true){
            
           window.location.href="/admin"
           }
           console.log(lastName);
           console.log(backendResult);
           
           

          })
        .catch(error => console.error('Error:', error))

    }

  }
  return (
    <div>
      <div className="modal fade" id="enroll" tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content">
            <form onSubmit={handler} >
              <div className='modal-header bg-secondary '>
                <p className='modal-title text-light fs-2'>Login</p>
              </div>
              <div className='modal-body col-sm-10 offset-md-1'>
                <div className='row mt-2 '>
                  <label for='userInput ' className='text-dark ' ><h5> Username </h5></label>
                  <input type='text' className='form-control border border-3' id='userInput' placeholder='Enter Username' name='username' />
                  <p id="error_username" className="d-none mb-0 text-danger"></p>
                  <label for='UserPassword ' className='text-dark mt-4 ' ><h5>Password</h5> </label>
                  <input type='password' className='form-control border border-3' id='UserPassword' placeholder='Enter Password' name='password' />
                  <p id="error_password" className="d-none mb-0 text-danger"></p>

                </div>


              </div>
              <div className='row mb-2 px-3'>
                <div className='col-8 gx-5'>
                  <span className='text-dark'>If you don't have account <a href='#'>RegisterHere</a> </span>
                </div>

                <div className='col-4'>
                  <button className='btn btn-secondary text-center '>Login</button>
                </div>
              </div>


            </form>
          </div>
        </div>
      </div>
    </div>
  )
}

export default LoginModal