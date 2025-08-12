import { Routes, Route } from 'react-router'
import Home from './modules/public/home/page'
import Auth from './modules/public/auth/page'
import Cart from './modules/customer/cart/page'
import Company from './modules/public/company/page'
import Favorite from './modules/customer/favorite/page'
import Order from './modules/customer/order/page'
import OrderDetails from './modules/customer/order-details/page'
import Product from './modules/public/product/page'
import ProductDetails from './modules/public/product-details/page'
import Profile from './modules/customer/profile/page'
import Question from './modules/seller/question/page'
import ReturnRequest from './modules/customer/return-request/page'
import Review from './modules/public/review/page'
import BrandManagement from './modules/admin/brand/page'
import CategoryManagement from './modules/admin/category/page'
import ShipperManagement from './modules/admin/shipper/page'
import VariationManagement from './modules/admin/variation/page'
import CompanyManagement from './modules/admin/company/page'
import CreateProduct from './modules/seller/create-product/page'
import CompanyDetails from './modules/seller/company-details/page'
import ProductStatistics from './modules/seller/product-statistics/page'
import SellerOrders from './modules/seller/orders/page'
import ReturnRequestManagement from './modules/seller/return-requests/page'
import SellerProfile from './modules/seller/profile/page'

function App() {
  return (
    <Routes>
      {/* PUBLIC */}
      <Route path='/' element={<Home />} />
      <Route path='/auth/:role/:method' element={<Auth />} />
      <Route path='/company/:id' element={<Company />} />
      <Route path='/product' element={<Product />} />
      <Route path='/product/:id' element={<ProductDetails />} />
      <Route path='/product/:id/review' element={<Review />} />

      {/* CUSTOMER */}
      <Route path='/profile' element={<Profile />} />
      <Route path='/cart' element={<Cart />} />
      <Route path='/favorite' element={<Favorite />} />
      <Route path='/order' element={<Order />} />
      <Route path='/order/:id' element={<OrderDetails />} />
      <Route path='/return-request' element={<ReturnRequest />} />

      {/* SELLER */}
      <Route path='/seller/company' element={<CompanyDetails />} />
      <Route path='/seller/product/:id' element={<ProductStatistics />} />
      <Route path='/seller/product/create' element={<CreateProduct />} />
      <Route path='/seller/question' element={<Question />} />
      <Route path='/seller/orders' element={<SellerOrders />} />
      <Route path='/seller/return-requests' element={<ReturnRequestManagement />} />
      <Route path='/seller/profile' element={<SellerProfile />} />

      {/* ADMIN */}
      <Route path='/admin/brand' element={<BrandManagement />} />
      <Route path='/admin/category' element={<CategoryManagement />} />
      <Route path='/admin/shipper' element={<ShipperManagement />} />
      <Route path='/admin/variation' element={<VariationManagement />} />
      <Route path='/admin/company' element={<CompanyManagement />} />
    </Routes>
  )
}

export default App
