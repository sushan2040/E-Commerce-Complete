import Card from "./Card";
import RemoveCartItem from "./CardRemoveItem";
import CommonScreen from "../../Structure/CommonScreen";
import axios from "axios";
import CONSTANTS from "../../utils/Constants";
import { useEffect } from "react";

export default function Cart() {
    return (
        <>
            <div className="row" style={{ display: 'flex', justifyContent: 'right' }}>
                <div className="col-sm-5">
                    <a className="btn btn-success">Checkout / Place order</a>
                </div>
            </div>
            <div className="row">
                <RemoveCartItem />
                <RemoveCartItem />
                <RemoveCartItem />
                <RemoveCartItem />
                <RemoveCartItem />
            </div>
        </>
    )
}