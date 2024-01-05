import { useEffect } from "react"
import useStateRef from "react-usestateref";
import { useNavigate } from "react-router-dom";


const ViewUsers = () => {
    const [usersList, setUsersList, refUsersList] = useStateRef([]);
    const [listofUserLogin, setListofUserLogin, refListofUserLogin] = useStateRef([]);
    var url = window.location.href.includes('?');
    var queryParam = window.location.search;
  
    const queryParamKeypairs = new URLSearchParams(queryParam);
  
    var partyIdOfAdmin = queryParamKeypairs.get('partyIdOfAdmin');

    useEffect(() => {
        const listOfUsersAsync = async () => {
            const ListOfUserDetailsFetch = await fetch("https://localhost:8443/onlineexamapplication/control/get-users", {
                method: "POST",
                credentials: "include",
                body: JSON.stringify({ partyIdOfAdmin: partyIdOfAdmin}),
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                }
            });

            const ListOfUserDetails = await ListOfUserDetailsFetch.json();
            console.log(ListOfUserDetails)
            setUsersList(ListOfUserDetails.ListOfUserDetails.ListOfUsers);
            setListofUserLogin(ListOfUserDetails.ListOfUserDetails.ListOfUserLoginId);
            console.log("thi si ")
            console.log(refUsersList.current)
        }
        listOfUsersAsync();
    }, []);

    var navigate=useNavigate();
    const userNavigation=(user,userLogin)=>{
        navigate('/user-exam-mapping',{state:{
           user:user,
           userLogin:userLogin
        }          
        })
        
    }

    return (
        <>
        <div className="container py-5">
            <div className="card-header">
                <h1>Users List</h1>
            </div>
            <div className="pb-5"style={{ "zIndex": "3000"}}>
                <table className='table w-70 shadow table-hover'>
                    <thead className="table-dark fs-6 ">
                        <tr >
                            <th className='p-4'>USERID</th>
                            <th className='p-4'>NAME</th>
                            <th className='p-4'>LAST NAME</th>

                        </tr>
                    </thead>
                    <tbody className="fs-6">
                        {refUsersList.current.length == 0 ? setTimeout(()=><h1>no users available</h1>,1000):refUsersList.current.map((user) => (
                            <>
                                {refListofUserLogin.current.map((userLogin) =>
                                (userLogin.partyId === user.partyId &&
                                    <tr onClick={()=>userNavigation(user,userLogin)} style={{"cursor":"pointer"}}>
                                        <td className='p-4 '>{userLogin.userLoginId}</td>
                                        <td className='p-4'>{user.firstName}</td>
                                        <td className='p-4'>{user.lastName}</td>
                                    </tr>
                                    
                                )
                                )}
                            </>

                        )
                        )} 
                       
                    </tbody>
                </table>
            </div>
            </div>
     </>
    )
}

export default ViewUsers
