import React, {useState,useEffect} from 'react';
import { KeyboardAvoidingView, StyleSheet,Image,Text, View, TextInput, TouchableOpacity, Keyboard, ScrollView} from 'react-native';
import Transfer from '../components/Transfer';
import { colors, GlobalStyles } from '../styles/globalStyles';
import Menu from '../components/Menu'
import { FloatingAction } from "react-native-floating-action";
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';

export default function Dashboard({navigation}) {
  const [transfer, setTransfer] = useState();
  const [transferItems, setTransferItems] = useState([]);
  const [userConnected, setUserConnected] = useState();
  const baseUrl = "http://192.168.43.39:8080/"
useEffect(async ()=>{
  try {
    const user = await AsyncStorage.getItem('user');
    await new Promise(resolve => { setTimeout(resolve, 10); });
    if(user != null){
      const currentUser = JSON.parse(user);
      setUserConnected(currentUser);
      let response =await axios.get(`${baseUrl}transferservice/transfer/${currentUser.client.id}`);
      const payload = Object.keys(response.data).map(key => ({ ...response.data[key], key }))
      setTransferItems(payload)
    }
  }catch(err){
    console.log(err);
  }
},[])
const actions = [
  {
    text: "Ajouter un transfert",
    icon: require("../assets/transfer.png"),
    name: "AddTransfer",
    position: 2
  }
];
  const handleAddTransfer = () => {
    navigation.reset({
      index: 0,
      routes: [{ name: 'AddTransfer' }],
    })
  }
  
  const reformateDate = (date) => {
    var date_transfer = new Date(date);
    var date_transfer_str = date_transfer.getFullYear() + "/" +
    ("0" + (date_transfer.getMonth()+1)).slice(-2) + "/" +
    ("0" + date_transfer.getDate()).slice(-2) + " à " +
    ("0" + date_transfer.getHours()).slice(-2) + ":" +
    ("0" + date_transfer.getMinutes()).slice(-2) ;
    return date_transfer_str;
  }
  return (
    <View style={GlobalStyles.container}>
      <ScrollView
        contentContainerStyle={{
          flexGrow: 1
        }}
        keyboardShouldPersistTaps='handled'
      >
      <View style={GlobalStyles.transfersWrapper}>
        <View style={GlobalStyles.items}>
          {
            transferItems.map((item, index) => {
              return (
                <TouchableOpacity key={index}  onPress={() =>{
                  navigation.navigate('TransferDetailsScreen',{item})
                }}>
                  <View style={GlobalStyles.item}>
                    <View style={GlobalStyles.itemLeft}>
                      <View style={GlobalStyles.square}>
                      <Image source={require('../assets/valid.png')} style={GlobalStyles.typeTransfer}/>
                      </View>
                      <View>
                        <Text style={GlobalStyles.itemText}>{item.transfer.montant} DH</Text>
                        <Text style={GlobalStyles.itemText2}>Effecuté le {reformateDate(item.transfer.transferDate)}</Text>
                      </View>
                    </View>
                    <View style={GlobalStyles.circular}>
                      <Image source={item.clientSrc.id != userConnected.client.id ? require('../assets/enter.png'):require('../assets/out.png')} style={GlobalStyles.typeTransfer}/>
                    </View>
                  </View>
                </TouchableOpacity>
              )
            })
          }
        </View>
      </View>
      </ScrollView>
      <FloatingAction
          actions={actions}
          onPressItem={name => {
            console.log(name)
            if(name=="AddTransfer") navigation.navigate('AddTransferScreen')
          }}
        />
    </View>
  );
}
