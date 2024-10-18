<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products Table</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">

<!-- Navigation Bar -->
<header class="bg-white shadow p-4">
    <div class="container mx-auto flex justify-between items-center">
        <h1 class="absolute left-20 text-2xl font-bold">SilkRoad</h1>
        <div class="absolute right-20 flex items-center space-x-4">

        </div>
    </div>
</header>

<div class="flex">
    <!-- Sidebar -->
    <aside class="w-1/10 bg-white border-r border-gray-300 p-4">
        <ul>
            <li class="py-2 hover:bg-gray-200">
                <a href="#" class="block text-gray-700">Home</a>
            </li>
            <li class="py-2 hover:bg-gray-200">
                <a href="#" class="block text-gray-700">Products</a>
            </li>
            <li class="py-2 hover:bg-gray-200">
                <a href="#" class="block text-gray-700">Orders</a>
            </li>
            <li class="py-2 hover:bg-gray-200">
                <a href="#" class="block text-gray-700">Customers</a>
            </li>
            <li class="py-2 hover:bg-gray-200">
                <a href="#" class="block text-gray-700">Reports</a>
            </li>
        </ul>
    </aside>

    <!-- Main Content -->
    <div class="flex-1 p-6">
        <h1 class="text-2xl font-bold mb-5">Products</h1>
        <div class="mb-4 flex justify-between">
            <div>
                <input type="text" placeholder="Search..." class="p-2 border border-gray-300 rounded">
                <select class="ml-2 p-2 border border-gray-300 rounded">
                    <option>Show All</option>
                    <option>Out of Stock</option>
                </select>
            </div>
            <button class="bg-blue-500 text-white px-4 py-2 rounded">New Product</button>
        </div>

        <table class="min-w-full bg-white border border-gray-300">
            <thead>
            <tr class="bg-gray-200 text-gray-700">
                <th class="py-2 px-4 border-b">#</th>
                <th class="py-2 px-4 border-b">Product Name</th>
                <th class="py-2 px-4 border-b">Category</th>
                <th class="py-2 px-4 border-b">SKU</th>
                <th class="py-2 px-4 border-b">Variant</th>
                <th class="py-2 px-4 border-b">Price</th>
                <th class="py-2 px-4 border-b">Status</th>
                <th class="py-2 px-4 border-b">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr class="hover:bg-gray-100">
                <td class="py-2 px-4 border-b">1</td>
                <td class="py-2 px-4 border-b">Solid Liquid Neck Blouse</td>
                <td class="py-2 px-4 border-b">CLOTHING</td>
                <td class="py-2 px-4 border-b">T156790</td>
                <td class="py-2 px-4 border-b">Varies on Size, Color</td>
                <td class="py-2 px-4 border-b">$24</td>
                <td class="py-2 px-4 border-b">
                    <span class="bg-green-500 text-white px-2 py-1 rounded">Active</span>
                </td>
                <td class="py-2 px-4 border-b">
                    <button class="text-blue-500">Edit</button>
                    <button class="text-red-500 ml-2">Delete</button>
                </td>
            </tr>
            <tr class="hover:bg-gray-100">
                <td class="py-2 px-4 border-b">2</td>
                <td class="py-2 px-4 border-b">Plant Toe Heeled Pumps</td>
                <td class="py-2 px-4 border-b">SHOES</td>
                <td class="py-2 px-4 border-b">T578443</td>
                <td class="py-2 px-4 border-b">4</td>
                <td class="py-2 px-4 border-b">$56</td>
                <td class="py-2 px-4 border-b">
                    <span class="bg-red-500 text-white px-2 py-1 rounded">Out of Stock</span>
                </td>
                <td class="py-2 px-4 border-b">
                    <button class="text-blue-500">Edit</button>
                    <button class="text-red-500 ml-2">Delete</button>
                </td>
            </tr>
            <!-- Add more rows as needed -->
            </tbody>
        </table>

        <div class="mt-4">
            <nav class="flex justify-between">
                <span class="text-gray-700">Showing 1 to 2 of 10 entries</span>
                <div class="flex space-x-2">
                    <button class="bg-gray-300 px-3 py-1 rounded">1</button>
                    <button class="bg-gray-300 px-3 py-1 rounded">2</button>
                    <button class="bg-gray-300 px-3 py-1 rounded">...</button>
                    <button class="bg-gray-300 px-3 py-1 rounded">10</button>
                </div>
            </nav>
        </div>
    </div>
</div>

</body>
</html>
