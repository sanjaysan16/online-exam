import React, { useEffect } from "react";
import useStateRef from "react-usestateref";
import { port, protocol } from "../fetchConst";

const LoginModal = () => {
  const [hasErrorRef, setHasError, refHasError] = useStateRef(false);

  const uri = `${protocol}://${window.location.hostname}:${port}`;

  const validateLoginForm = (key, value) => {
    switch (key) {
      case "USERNAME":
        var validation = "[a-z0-9]+[@]{1}[a-z]{5}[.]{1}[com]";
        var regex = RegExp(validation);

        if (value === "") {
          console.log("i am empty username");
          document.getElementById("error_username").classList.remove("d-none");
          document.getElementById("error_username").classList.add("d-block");
          document.getElementById("error_username").innerHTML =
            "*please enter a username";
          setHasError(true);
        } else {
          console.log(!regex.test(value), "i am regex");
          if (!regex.test(value)) {
            setHasError(true);
            document
              .getElementById("error_username")
              .classList.remove("d-none");
            document.getElementById("error_username").classList.add("d-block");
            document.getElementById("error_username").innerHTML =
              "*please enter valid username";
          }
        }
        break;
      case "PASSWORD":
        if (value === "") {
          document.getElementById("error_password").classList.remove("d-none");
          document.getElementById("error_password").classList.add("d-block");
          document.getElementById("error_password").innerHTML =
            "*please enter a password";
          setHasError(true);
        } else {
          document.getElementById("error_password").classList.add("d-none");
        }
    }
  };

  const handler = (e) => {
    e.preventDefault();
    setHasError(false);
    const data = new FormData(e.target);
    let username = data.get("USERNAME");
    console.log(username);
    let password = data.get("PASSWORD");
    console.log(password);
    const value = Object.fromEntries(data.entries());
    document.getElementById("error_username").classList.add("d-none");
    document.getElementById("error_password").classList.add("d-none");
    document.getElementById("error_user").classList.add("d-none");
    Object.entries(value).map(([key, value]) => {
      validateLoginForm(key, value);
    });

    if (!refHasError.current) {
      fetch(`${uri}/onlineexamapplication/control/login`, {
        method: "POST",
        credentials: "include",
        body: JSON.stringify({ USERNAME: username, PASSWORD: password }),
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
      })
        .then((response) => response.json())
        .then((values) => {
          console.log(values);

          const backendResult = values.isAdmin;
          const lastName = values._lastName_;
          if (values._ERROR_MESSAGE_ != null) {
            document.getElementById("error_user").classList.remove("d-none");
            document.getElementById("error_user").classList.add("d-block");
            document.getElementById("error_user").innerHTML =
              values._ERROR_MESSAGE_;
          }
          const errorMessage = values._ERROR_MESSAGE_;
          console.log("first", errorMessage);
          if (backendResult === true) {
            window.location.href = "/dash-bord";
          } else {
            window.location.href = "/admin-view";
          }
          console.log(lastName);
          console.log(backendResult);
        })
        .catch((error) => console.error("Error:", error));
    }
  };
  return (
    <div>
      <div
        className="modal fade"
        id="enroll"
        tabIndex="-1"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content">
            <form onSubmit={handler}>
              <div className="modal-header bg-secondary ">
                <p className="modal-title text-light fs-2">Login</p>
              </div>

              <div className="modal-body col-sm-10 offset-md-1">
                <p id="error_user" className="d-none mb-0 text-danger"></p>
                <div className="row mt-2 ">
                  <label for="userInput " className="text-dark ">
                    <h5> Username </h5>
                  </label>
                  <input
                    type="text"
                    className="form-control border border-3"
                    id="userInput"
                    placeholder="Enter Username"
                    name="USERNAME"
                  />
                  <p
                    id="error_username"
                    className="d-none mb-0 text-danger"
                  ></p>
                  <label for="UserPassword " className="text-dark mt-4 ">
                    <h5>Password</h5>{" "}
                  </label>
                  <input
                    type="password"
                    className="form-control border border-3"
                    id="UserPassword"
                    placeholder="Enter Password"
                    name="PASSWORD"
                  />
                  <p
                    id="error_password"
                    className="d-none mb-0 text-danger"
                  ></p>
                </div>
              </div>
              <div className="row mb-2 px-3">
                <div className="col-8 gx-5">
                  <span className="text-dark">
                    If you don't have account{" "}
                    <a href="register">RegisterHere</a>{" "}
                  </span>
                </div>

                <div className="col-4">
                  <button
                    className="btn btn-secondary text-center "
                    type="submit"
                  >
                    Login
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginModal;
