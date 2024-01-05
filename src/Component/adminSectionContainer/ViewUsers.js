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
        
        listOfUsersAsync();
    }, []);
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
        setUsersList(ListOfUserDetails.ListOfUserDetails);
    }

    var navigate=useNavigate();
    const userNavigation=(user)=>{
        navigate('/user-exam-mapping',{state:{
           user:user
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
                        
                        {refUsersList.current.length === 0 ?
                        <tr>
                                       <td className='p-4 '></td>
                                        <td className='p-4 text-center text-danger'>*no users available</td>
                                        <td className='p-4'></td>
                        </tr>
                        : refUsersList.current&&refUsersList.current.map((user) => (
                                    <tr onClick={()=>userNavigation(user)} style={{"cursor":"pointer"}}>
                                        <td className='p-4 '>{user.userLoginId}</td>
                                        <td className='p-4'>{user.firstName}</td>
                                        <td className='p-4'>{user.lastName}</td>
                                    </tr>
                                    
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
